package com.github.edgar615.util.spring.cache;

import com.github.edgar615.util.spring.cache.dynamic.DynamicCacheName;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CacheServiceImpl implements CacheService {
  @Override
  @Cacheable(cacheNames = "caffeineCache1", key = "#p0")
  public String getCache1(int id) {
    System.out.println("cache1");
    return UUID.randomUUID().toString();
  }

  @Override
  @Cacheable(cacheNames = "caffeineCache2", key = "#p0")
  public String getCache2(int id) {
    System.out.println("cache2");
    return UUID.randomUUID().toString();
  }

  @Override
  @Cacheable(cacheNames = "caffeineCache1", key = "#p0")
  @DynamicCacheName("#p0")
  public String dynamic(int id) {
    System.out.println("dynamic");
    return UUID.randomUUID().toString();
  }

  @Override
  @Cacheable(cacheNames = "l2Cache", key = "#p0")
  public String l2Cache(int id) {
    System.out.println("l2Cache");
    return UUID.randomUUID().toString();
  }
}
