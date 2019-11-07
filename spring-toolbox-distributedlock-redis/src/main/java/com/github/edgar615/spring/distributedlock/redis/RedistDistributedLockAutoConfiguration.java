package com.github.edgar615.spring.distributedlock.redis;

import com.github.edgar615.spring.distributedlock.DistributedLockProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
//@ConditionalOnBean(RedisConnectionFactory.class)
public class RedistDistributedLockAutoConfiguration {

  @Bean(name = "simpleRedistDistributedLockProvider")
  @ConditionalOnMissingBean
  public DistributedLockProvider distributedLockProvider(
      RedisTemplate<String, String> redisTemplate) {
    return new SimpleRedistDistributedLockProvider(redisTemplate);
  }
}
