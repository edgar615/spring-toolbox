package com.github.edgar615.util.spring.eventbus;

import com.github.edgar615.util.eventbus.EventConsumer;
import com.github.edgar615.util.eventbus.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class EventbusPropertiesAutoConfiguration {

  @Autowired
  private ApplicationContext applicationContext;

  @Bean
  @ConditionalOnMissingBean(EventProducer.class)
  public EventProducer eventProducer() {
    return new SpringEventProducer();
  }

  @Bean
  @ConditionalOnMissingBean(EventConsumer.class)
  public EventConsumer eventConsumer() {
    return new SpringEventConsumer();
  }

}
