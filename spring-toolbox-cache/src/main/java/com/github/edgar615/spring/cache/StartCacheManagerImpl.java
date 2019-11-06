package com.github.edgar615.spring.cache;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/5/22.
 *
 * @author Edgar  Date 2018/5/22
 */
class StartCacheManagerImpl implements StartCacheManager {
  private final Collection<? extends StartCache> caches;

  StartCacheManagerImpl(Collection<? extends StartCache> caches) {
    Objects.requireNonNull(caches);
    this.caches = caches;
  }

  @Override
  public synchronized StartCache getCache(String name) {
    Optional<?> optional = this.caches.stream()
            .filter(c -> c.name().equals(name))
            .findFirst();
    if (optional.isPresent()) {
      return (StartCache) optional.get();
    }
    return null;
  }

  @Override
  public synchronized Collection<String> getCacheNames() {
    return this.caches.stream()
            .map(c -> c.name())
            .collect(Collectors.toList());
  }

  /**
   * 首次加载数据
   */
  @Override
  public void start() {
    for (StartCache cache : caches) {
      cache.load();
    }
  }
}
