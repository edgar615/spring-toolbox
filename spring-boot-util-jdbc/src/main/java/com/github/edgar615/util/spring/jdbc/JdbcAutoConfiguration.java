package com.github.edgar615.util.spring.jdbc;

import com.github.edgar615.util.db.Jdbc;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
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
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({JdbcCacheProperties.class})
public class JdbcAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean({Jdbc.class})
  @ConditionalOnBean(DataSource.class)
  public Jdbc jdbc(@Autowired JdbcOperation jdbcOperation) {
    return new JdbcImpl(jdbcOperation);
  }

  @Bean
  @ConditionalOnMissingBean({JdbcOperation.class})
  @ConditionalOnBean(DataSource.class)
  @ConditionalOnProperty(name = "jdbc.caching.enabled", matchIfMissing = true, havingValue = "false")
  public JdbcOperation jdbcOperation(@Autowired DataSource dataSource) {
    return new JdbcOperationImpl(dataSource);
  }

  @Bean
  @ConditionalOnMissingBean({JdbcOperation.class})
  @ConditionalOnBean({DataSource.class, CacheManager.class})
  @ConditionalOnProperty(name = "jdbc.caching.enabled", matchIfMissing = false, havingValue = "true")
  public JdbcOperation cachedJdbcOperation(@Autowired DataSource dataSource, @Autowired FindByIdAction findByIdAction) {
    return new CachedJdbcOperationImpl(dataSource, findByIdAction);
  }

  @Bean
  @ConditionalOnMissingBean({FindByIdAction.class})
  @ConditionalOnBean({DataSource.class, CacheManager.class})
  @ConditionalOnProperty(name = "jdbc.caching.enabled", matchIfMissing = false, havingValue = "true")
  public FindByIdAction findByIdAction(@Autowired DataSource dataSource) {
    return new FindByIdActionImpl(dataSource);
  }

  @Bean
  @ConditionalOnMissingBean({JdbcCacheResolver.class})
  @ConditionalOnBean(CacheManager.class)
  public JdbcCacheResolver jdbcCacheResolver(@Autowired CacheManager cacheManager, JdbcCacheProperties jdbcCacheProperties) {
    return new JdbcCacheResolver(cacheManager, jdbcCacheProperties);
  }

}
