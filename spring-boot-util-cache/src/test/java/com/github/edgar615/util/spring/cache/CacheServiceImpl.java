package com.github.edgar615.util.spring.cache;

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
}
