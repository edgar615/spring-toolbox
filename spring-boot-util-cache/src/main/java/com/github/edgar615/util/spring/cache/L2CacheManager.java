package com.github.edgar615.util.spring.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Edgar on 2018/8/15.
 *
 * @author Edgar  Date 2018/8/15
 */
public class L2CacheManager implements CacheManager {
  private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

  @Override
  public Cache getCache(String name) {
    return cacheMap.get(name);
  }

  @Override
  public Collection<String> getCacheNames() {
    return null;
  }

  public void addCache(@Nullable Collection<L2Cache> caches) {
    Objects.requireNonNull(caches);
    for (L2Cache cache : caches) {
      this.cacheMap.put(cache.getName(), cache);
    }
  }
}
