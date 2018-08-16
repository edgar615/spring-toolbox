package com.github.edgar615.util.spring.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.cache.CacheType;
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
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

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
    caches.addAll(caches);


    //要先定义好缓存，然后才能定义二级缓存
    List<Cache> l2Cache = properties.getL2Cache().getSpec().entrySet().stream()
            .map(spec -> new L2Cache(spec.getKey(), findByName(caches, spec.getValue().getL1()),
                                     findByName(caches, spec.getValue().getL2()), true))
            .collect(Collectors.toList());
    caches.addAll(l2Cache);
    SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    simpleCacheManager.setCaches(caches);
    simpleCacheManager.initializeCaches();

    CompositeCacheManager cacheManager = new CompositeCacheManager(simpleCacheManager);
//    if (properties.getDynamic() != null
//        && !properties.getDynamic().isEmpty()) {
//      List<CacheConfig> dynamicCacheConfig = properties.getDynamic().stream().map(this::config)
//              .collect(Collectors.toList());
//      cacheManager = new DynamicCacheManager(dynamicCacheConfig);
//    }
    return cacheManager;
  }

  private Cache findByName(List<Cache> caches, String name) {
   Optional<Cache> optional = caches.stream().filter(c -> name.equals(c.getName()))
            .findFirst();
    if (optional.isPresent()) {
      return optional.get();
    }
    throw new NullPointerException("cache:" + name);
  }

  static class CacheConfigurationImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
      CacheType[] types = CacheType.values();
      String[] imports = new String[types.length];
//      for (int i = 0; i < types.length; i++) {
//        imports[i] = CacheConfigurations.getConfigurationClass(types[i]);
//      }
      return imports;
    }

  }

}
