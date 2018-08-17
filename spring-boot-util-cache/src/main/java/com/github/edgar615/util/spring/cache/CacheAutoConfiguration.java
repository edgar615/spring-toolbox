package com.github.edgar615.util.spring.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
@EnableConfigurationProperties({CacheProperties.class})
public class CacheAutoConfiguration {

  @Bean
  @ConditionalOnProperty(name = "caching.enabled", matchIfMissing = false, havingValue = "true")
  @ConditionalOnMissingBean(CacheManager.class)
  public CacheManager cacheManager(CacheProperties properties) {
    List<Cache> caches = new ArrayList<>();
    List<Cache> caffeine = properties.getCaffeine().getSpec().entrySet().stream()
            .map(spec -> new CaffeineCache(spec.getKey(), Caffeine.from(spec.getValue()).build()))
            .collect(Collectors.toList());
    caches.addAll(caffeine);
    SimpleCacheManager caffeineManager = new SimpleCacheManager();
    caffeineManager.setCaches(caches);
    caffeineManager.initializeCaches();
    List<CacheManager> cacheManagers = new ArrayList<>();
    cacheManagers.add(caffeineManager);

    //要先定义好缓存，然后才能定义二级缓存
    List<Cache> l2Cache = properties.getL2Cache().getSpec().entrySet().stream()
            .map(spec -> new L2Cache(spec.getKey(), findByName(caches, spec.getValue().getL1()),
                                     findByName(caches, spec.getValue().getL2()), true))
            .collect(Collectors.toList());
    if (!l2Cache.isEmpty()) {
      SimpleCacheManager l2CacheManager = new SimpleCacheManager();
      l2CacheManager.setCaches(l2Cache);
      l2CacheManager.initializeCaches();
      cacheManagers.add(l2CacheManager);
    }
    CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
    compositeCacheManager.setCacheManagers(cacheManagers);
    compositeCacheManager.afterPropertiesSet();
    return compositeCacheManager;
  }

  @Bean
  @ConditionalOnBean(CacheManager.class)
  public CacheEvictEventListener cacheEvictEventListener(CacheManager cacheManager) {
    return new CacheEvictEventListener(cacheManager);
  }

  private Cache findByName(List<Cache> caches, String name) {
    Optional<Cache> optional = caches.stream().filter(c -> name.equals(c.getName()))
            .findFirst();
    if (optional.isPresent()) {
      return optional.get();
    }
    throw new NullPointerException("cache:" + name);
  }

}
