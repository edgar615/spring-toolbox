package com.github.edgar615.util.mybatis;

import com.github.edgar615.util.spring.jdbc.JdbcCacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JdbcCacheProperties.class})
public class MybatisAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean({MybatisJdbcCacheResolver.class})
  @ConditionalOnBean(CacheManager.class)
  public MybatisJdbcCacheResolver jdbcCacheResolver(CacheManager cacheManager, JdbcCacheProperties jdbcCacheProperties) {
    return new MybatisJdbcCacheResolver(cacheManager, jdbcCacheProperties);
  }

}
