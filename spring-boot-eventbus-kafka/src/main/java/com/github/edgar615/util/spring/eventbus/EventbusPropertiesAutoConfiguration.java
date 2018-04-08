package com.github.edgar615.util.spring.eventbus;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import com.github.edgar615.util.eventbus.EventConsumer;
import com.github.edgar615.util.eventbus.EventProducer;
import com.github.edgar615.util.eventbus.KafkaConsumerOptions;
import com.github.edgar615.util.eventbus.KafkaEventConsumer;
import com.github.edgar615.util.eventbus.KafkaEventProducer;
import com.github.edgar615.util.eventbus.KafkaProducerOptions;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by Administrator on 2017/10/7.
 *
 * @ConditionalOnBean：当容器里有指定的Bean 的条件下。
 * @ConditionalOnClass：当类路径下有指定的类的条件下。
 * @ConditionalOnExpression：基于SpEL 表达式作为判断条件。
 * @ConditionalOnJava：基于JVM 版本作为判断条件。
 * @ConditionalOnJndi：在JNDI 存在的条件下查找指定的位置。
 * @ConditionalOnMissingBean：当容器里没有指定Bean 的情况下。
 * @ConditionalOnMissingClass：当类路径下没有指定的类的条件下。
 * @ConditionalOnNotWebApplication：当前项目不是Web 项目的条件下。
 * @ConditionalOnProperty：指定的属性是否有指定的值。
 * @ConditionalOnResource：类路径是否有指定的值。
 * @ConditionalOnSingleCandidate：当指定Bean 在容器中只有一个，或者虽然有多个但是指定首选的Bean。
 * @ConditionalOnWebApplication：当前项目是Web 项目的条件下。
 */
@Configuration
@EnableConfigurationProperties({EventbusProducerProperties.class, EventbusConsumerProperties.class})
public class EventbusPropertiesAutoConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "eventbus.producer", name = "servers", matchIfMissing = false)
  @ConditionalOnMissingBean(KafkaEventProducer.class)
  public EventProducer eventProducer(EventbusProducerProperties properties) {
    KafkaProducerOptions options = new KafkaProducerOptions()
            .setServers(properties.getServers())
            .setAcks(properties.getAcks())
            .setBatchSize(properties.getBatchSize())
            .setBufferMemory(properties.getBufferMemory())
            .setLingerMs(properties.getLingerMs())
            .setRetries(properties.getRetries())
            .setPartitionClass(properties.getPartitionClass())
            .setFetchPendingPeriod(properties.getFetchPendingPeriod())
            .setMaxQuota(properties.getMaxQuota());
    return new KafkaEventProducer(options);
  }

  @Bean
  @ConditionalOnProperty(prefix = "eventbus.consumer", name = {"servers", "group"},
          matchIfMissing = false)
  @ConditionalOnMissingBean(EventConsumer.class)
  public EventConsumer eventConsumer(EventbusConsumerProperties properties) {
    KafkaConsumerOptions options = new KafkaConsumerOptions()
            .setServers(properties.getServers())
            .setMaxQuota(properties.getMaxQuota())
            .setBlockedCheckerMs(properties.getBlockedCheckerMs())
            .setConsumerAutoOffsetRest(properties.getConsumerAutoOffsetReset())
            .setConsumerSessionTimeoutMs(properties.getConsumerSessionTimeoutMs())
            .setGroup(properties.getGroup())
            .setWorkerPoolSize(properties.getWorkerPoolSize());
    if (!Strings.isNullOrEmpty(properties.getPattern())) {
      options.setPattern(properties.getPattern());
    }
    for (String topic : properties.getTopics()) {
      options.addTopic(topic);
    }
    for (String offset : properties.getOffsets()) {
      List<String> offsets = Splitter.on(",").omitEmptyStrings().trimResults()
              .splitToList(offset);
      TopicPartition tp = new TopicPartition(offsets.get(0), Integer.parseInt(offsets.get(1)));
      options.addStartingOffset(tp, Long.parseLong(offsets.get(2)));
    }
    return new KafkaEventConsumer(options);
  }
}
