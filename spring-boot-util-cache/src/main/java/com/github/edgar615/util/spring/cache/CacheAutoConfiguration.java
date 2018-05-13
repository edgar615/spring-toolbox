package com.github.edgar615.util.spring.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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

  @Bean
  @ConditionalOnProperty(name = "cache.enabled", matchIfMissing = false, havingValue = "true")
  @ConditionalOnMissingBean(CacheManager.class)
  public CacheManager cacheManager(CacheProperties properties) {
    List<Cache> caches = new ArrayList<>();
    List<CacheConfig> cacheConfigs = properties.getSpec().stream().map(this::config)
            .collect(Collectors.toList());
    for (CacheConfig cacheConfig : cacheConfigs) {
      if ("caffeine".equalsIgnoreCase(cacheConfig.getType())) {
        Caffeine caffeine = Caffeine.newBuilder();
        if (cacheConfig.getMaximumSize() != null) {
          caffeine.maximumSize(cacheConfig.getMaximumSize());
        }
        if (cacheConfig.getExpireAfterWrite() != null) {
          caffeine.expireAfterWrite(cacheConfig.getExpireAfterWrite(), TimeUnit.SECONDS);
        }
        if (cacheConfig.getExpireAfterAccess() != null) {
          caffeine.expireAfterAccess(cacheConfig.getExpireAfterAccess(), TimeUnit.SECONDS);
        }
        if (cacheConfig.getRefreshAfterWrite() != null) {
          caffeine.refreshAfterWrite(cacheConfig.getRefreshAfterWrite(), TimeUnit.SECONDS);
        }
        caches.add(new CaffeineCache(cacheConfig.getName(), caffeine.build()));
      }

    }
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    cacheManager.setCaches(caches);
    return cacheManager;
  }

  public CacheConfig config(String spec) {
    List<String> options = Splitter.on(SPLIT_OPTIONS)
            .trimResults().omitEmptyStrings()
            .splitToList(spec);
    CacheConfig cacheConfig = new CacheConfig();
    for (String option : options) {
      String[] keyAndValue = option.split(SPLIT_KEY_VALUE);
      Preconditions.checkArgument(keyAndValue.length <= 2,
              "key-value pair %s with more than one equals sign", option);
      String key = keyAndValue[0].trim();
      String value = (keyAndValue.length == 1) ? null : keyAndValue[1].trim();
      if (key.equalsIgnoreCase("type")) {
        cacheConfig.setType(value);
      } else if (key.equalsIgnoreCase("name")) {
        cacheConfig.setName(value);
      } else if (key.equalsIgnoreCase("maximumSize")) {
        cacheConfig.setMaximumSize(Long.parseLong(value));
      } else if (key.equalsIgnoreCase("expireAfterAccess")) {
        TimeUnit timeUnit = parseTimeUnit(key, value);
        long duration = parseDuration(key, value);
        cacheConfig.setExpireAfterAccess(timeUnit.toSeconds(duration));
      } else if (key.equalsIgnoreCase("expireAfterWrite")) {
        TimeUnit timeUnit = parseTimeUnit(key, value);
        long duration = parseDuration(key, value);
        cacheConfig.setExpireAfterWrite(timeUnit.toSeconds(duration));
      } else if (key.equalsIgnoreCase("refreshAfterWrite")) {
        TimeUnit timeUnit = parseTimeUnit(key, value);
        long duration = parseDuration(key, value);
        cacheConfig.setRefreshAfterWrite(timeUnit.toSeconds(duration));
      }
    }
    return cacheConfig;
  }

  private static long parseDuration(String key, String value) {
    Preconditions.checkArgument((value != null) && !value.isEmpty(), String.format("value of key %s omitted", key));
    String duration = value.substring(0, value.length() - 1);
    return Long.parseLong(duration);
  }

  private TimeUnit parseTimeUnit(String key, String value) {
    Preconditions.checkArgument((value != null) && !value.isEmpty(), String.format("value of key %s omitted", key));
    char lastChar = Character.toLowerCase(value.charAt(value.length() - 1));
    switch (lastChar) {
      case 'd':
        return TimeUnit.DAYS;
      case 'h':
        return TimeUnit.HOURS;
      case 'm':
        return TimeUnit.MINUTES;
      case 's':
        return TimeUnit.SECONDS;
      default:
        throw new IllegalArgumentException(String.format(
                "key %s invalid format; was %s, must end with one of [dDhHmMsS]", key, value));
    }
  }

}
