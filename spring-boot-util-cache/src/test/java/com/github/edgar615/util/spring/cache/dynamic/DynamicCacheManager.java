package com.github.edgar615.util.spring.cache.dynamic;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.edgar615.util.spring.cache.CacheProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;

import java.util.Optional;

/**
 * 用的很少，而且有条件限制，暂时废弃这个功能.
 *
 * @author Edgar  Date 2018/6/23
 */
@Deprecated
public class DynamicCacheManager extends SimpleCacheManager {

  private final CacheProperties cacheProperties;

  public DynamicCacheManager(CacheProperties cacheProperties) {
    this.cacheProperties = cacheProperties;
  }

  @Override
  protected Cache getMissingCache(String name) {
    Optional<String> spec = cacheProperties.getCaffeine().getSpec().keySet().stream()
            .filter(c -> name.startsWith(c))
            .findFirst();
    if (!spec.isPresent()) {
      return super.getMissingCache(name);
    }
    String caffeineSpec = cacheProperties.getCaffeine().getSpec().get(spec.get());
    Cache cache = new CaffeineCache(name, Caffeine.from(caffeineSpec).build());
    return cache;
  }
}
