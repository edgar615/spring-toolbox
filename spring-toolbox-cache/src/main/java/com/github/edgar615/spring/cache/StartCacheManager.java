package com.github.edgar615.spring.cache;

import java.util.Collection;

/**
 * Created by Edgar on 2018/5/22.
 *
 * @author Edgar  Date 2018/5/22
 */
public interface StartCacheManager {
  StartCache getCache(String name);

  Collection<String> getCacheNames();

  void start();

  static StartCacheManager create(Collection<? extends StartCache> caches) {
    return new StartCacheManagerImpl(caches);
  }
}
