package com.github.edgar615.spring.cache;

public interface CacheService {
  String getCache1(int id);

  String getCache2(int id);

  String l2Cache(int id);

  String clearCache1();

  String evictCache1(int id);

  String clearL2Cache();

  String evictL2Cache(int id);

  int count(String method);

  String noOp(int id);

  void clearStat();
}
