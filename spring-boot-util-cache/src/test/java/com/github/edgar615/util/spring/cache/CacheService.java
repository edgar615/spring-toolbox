package com.github.edgar615.util.spring.cache;

public interface CacheService {
  String getCache1(int id);

  String getCache2(int id);

  String dynamic(int id);

  String l2Cache(int id);

  String clearCache1();

  String evict(int id);
}
