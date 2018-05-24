package com.github.edgar615.util.spring.eventbus;

import com.github.edgar615.util.eventbus.*;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

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

  @Autowired
  private ApplicationContext applicationContext;

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
    Map<String, ProducerStorage> map = applicationContext.getBeansOfType(ProducerStorage.class);
//    ProducerStorage storage = beanFactory.getBean(ProducerStorage.class);
    if (map.isEmpty()) {
      return new KafkaEventProducer(options);
    }
    return new KafkaEventProducer(options, map.values().iterator().next());
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
            .setMaxPollRecords(properties.getMaxPollRecords())
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
    Map<String, ConsumerStorage> consumerStorageMap = applicationContext.getBeansOfType(ConsumerStorage.class);
    Map<String, IdentificationExtractor> identificationExtractorMap = applicationContext.getBeansOfType(IdentificationExtractor.class);
    Map<String, BlackListFilter> blackListFilterMap = applicationContext.getBeansOfType(BlackListFilter.class);
    ConsumerStorage storage = consumerStorageMap.isEmpty() ? null : consumerStorageMap.values().iterator().next();
    IdentificationExtractor identificationExtractor = identificationExtractorMap.isEmpty() ? null : identificationExtractorMap.values().iterator().next();
    BlackListFilter blackListFilter = blackListFilterMap.isEmpty() ? null : blackListFilterMap.values().iterator().next();
    return new KafkaEventConsumer(options, storage, identificationExtractor, blackListFilter);
  }

}
