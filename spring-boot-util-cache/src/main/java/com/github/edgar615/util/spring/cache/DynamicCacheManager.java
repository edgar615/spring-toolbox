package com.github.edgar615.util.spring.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleCacheManager;

import java.util.List;
import java.util.Optional;

/**
 * Created by Edgar on 2018/6/23.
 *
 * @author Edgar  Date 2018/6/23
 */
public class DynamicCacheManager extends SimpleCacheManager {

  private final List<CacheConfig> configs;

  public DynamicCacheManager(List<CacheConfig> configs) {this.configs = configs;}

  @Override
  protected Cache getMissingCache(String name) {
    Optional<CacheConfig> cacheConfig = configs.stream()
            .filter(c -> name.startsWith(c.getName()))
            .findFirst();
    if (!cacheConfig.isPresent()) {
      return super.getMissingCache(name);
    }
    Cache cache = CacheUtils.createCache(cacheConfig.get());
    return cache;
  }
}
