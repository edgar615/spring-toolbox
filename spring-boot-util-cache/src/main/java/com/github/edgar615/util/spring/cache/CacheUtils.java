package com.github.edgar615.util.spring.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.concurrent.TimeUnit;

/**
 * Created by Edgar on 2018/6/23.
 *
 * @author Edgar  Date 2018/6/23
 */
public class CacheUtils {

  public static Cache createCache(CacheConfig cacheConfig) {
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
      return new CaffeineCache(cacheConfig.getName(), caffeine.build());
    }
    return null;
  }
}
