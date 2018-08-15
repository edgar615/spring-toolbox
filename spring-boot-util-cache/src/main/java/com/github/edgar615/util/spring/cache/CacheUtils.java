package com.github.edgar615.util.spring.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

/**
 * Created by Edgar on 2018/6/23.
 *
 * @author Edgar  Date 2018/6/23
 */
public class CacheUtils {

  public static Cache caffeine(String name, String spec) {
    return new CaffeineCache(name, Caffeine.from(spec).build());
  }
}
