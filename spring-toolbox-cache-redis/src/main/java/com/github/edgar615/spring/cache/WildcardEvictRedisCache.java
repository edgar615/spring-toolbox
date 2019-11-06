package com.github.edgar615.spring.cache;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * 支持使用前缀删除key
 */
public class WildcardEvictRedisCache extends RedisCache {

  private static final String WILDCARD = "wildcard:";

  private final RedisCacheWriter cacheWriter;

  private final ConversionService conversionService;

  private final String name;

  /**
   * Create new {@link RedisCache}.
   *
   * @param name must not be {@literal null}.
   * @param cacheWriter must not be {@literal null}.
   * @param cacheConfig must not be {@literal null}.
   */
  protected WildcardEvictRedisCache(String name,
      RedisCacheWriter cacheWriter,
      RedisCacheConfiguration cacheConfig) {
    super(name, cacheWriter, cacheConfig);
    this.name = name;
    this.cacheWriter = cacheWriter;
    this.conversionService = cacheConfig.getConversionService();
  }

  @Override
  public void evict(Object key) {
    if ((key instanceof String) && key.toString().startsWith(WILDCARD)) {
      String cacheKey = key.toString().substring(WILDCARD.length());
      byte[] pattern = createAndConvertCacheKey(cacheKey);
      cacheWriter.clean(name, pattern);
    } else {
      cacheWriter.remove(name, createAndConvertCacheKey(key));
    }
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.cache.Cache#clear()
   */
  @Override
  public void clear() {

    byte[] pattern = conversionService.convert(createCacheKey("*"), byte[].class);
    cacheWriter.clean(name, pattern);
  }

  private byte[] createAndConvertCacheKey(Object key) {
    return serializeCacheKey(createCacheKey(key));
  }

}
