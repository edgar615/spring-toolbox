package com.github.edgar615.spring.cache;

import java.util.Map;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

public class WildcardEvictRedisCacheManager extends RedisCacheManager {

  private final RedisCacheWriter cacheWriter;

  private final RedisCacheConfiguration defaultCacheConfig;

  public WildcardEvictRedisCacheManager(
      RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
      Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
    super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
    this.cacheWriter = cacheWriter;
    this.defaultCacheConfig = defaultCacheConfiguration;
  }

  @Override
  public RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
    return new WildcardEvictRedisCache(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);
  }
}
