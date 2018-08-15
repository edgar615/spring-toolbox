package com.github.edgar615.util.spring.cache;

import com.google.common.base.Preconditions;

import org.springframework.boot.autoconfigure.cache.CacheConfigurations;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;
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

  private static final String SPLIT_OPTIONS = ",";

  private static final String SPLIT_KEY_VALUE = "=";

  private static long parseDuration(String key, String value) {
    Preconditions.checkArgument((value != null) && !value.isEmpty(),
                                String.format("value of key %s omitted", key));
    String duration = value.substring(0, value.length() - 1);
    return Long.parseLong(duration);
  }

  @Bean
  @ConditionalOnProperty(name = "caching.enabled", matchIfMissing = false, havingValue = "true")
  @ConditionalOnMissingBean(CacheManager.class)
  public CacheManager cacheManager(CacheProperties properties) {
    List<Cache> caches = properties.getCaffeine().getSpec().entrySet().stream()
            .map(spec -> CacheUtils.caffeine(spec.getKey(), spec.getValue()))
            .collect(Collectors.toList());
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
