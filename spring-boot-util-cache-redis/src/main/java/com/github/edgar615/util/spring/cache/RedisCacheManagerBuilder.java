package com.github.edgar615.util.spring.cache;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class RedisCacheManagerBuilder {

  public RedisCacheManager build(RedisConnectionFactory redisConnectionFactory,
                                 CacheProperties cacheProperties) {
    Map<String, RedisCacheConfiguration> initialCacheConfigurations = new HashMap<>();
    for (String cacheName : cacheProperties.getRedis().getSpec().keySet()) {
      RedisCacheConfiguration configuration = redisCacheConfiguration(cacheProperties.getRedis().getSpec().get(cacheName));
      initialCacheConfigurations.put(cacheName, configuration);
    }
    RedisCacheManager redisCacheManager = new RedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
            RedisCacheConfiguration.defaultCacheConfig(), initialCacheConfigurations, false);
    redisCacheManager.initializeCaches();
    return redisCacheManager;
  }

  private RedisCacheConfiguration redisCacheConfiguration(RedisCacheSpec spec) {
    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
    if (spec.getTimeToLive() > 0) {
      config = config.entryTtl(Duration.ofMillis(spec.getTimeToLive()));
    }
    if (spec.getKeyPrefix() != null) {
      config = config.prefixKeysWith(spec.getKeyPrefix());
    }
    if (!spec.isCacheNullValues()) {
      config = config.disableCachingNullValues();
    }
    if (!spec.isUseKeyPrefix()) {
      config = config.disableKeyPrefix();
    }
    return config;
  }
}
