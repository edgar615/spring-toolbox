package com.github.edgar615.util.spring.jdbc;

import java.util.concurrent.Callable;
import org.springframework.cache.support.AbstractValueAdaptingCache;

/**
 * 专门做的一个不做使用缓存的cache，用于在CacheResolver返回不需要使用缓存的对象
 */
public class NoCachingCache extends AbstractValueAdaptingCache {

  private static final NoCachingCache INSTANCE = new NoCachingCache();

  private NoCachingCache() {
    super(false);
  }

  public static NoCachingCache instance() {
    return INSTANCE;
  }

  @Override
  protected Object lookup(Object key) {
    return null;
  }

  @Override
  public String getName() {
    return "NoCachingCache";
  }

  @Override
  public Object getNativeCache() {
    return null;
  }

  @Override
  public <T> T get(Object key, Callable<T> valueLoader) {
    return null;
  }

  @Override
  public void put(Object key, Object value) {

  }

  @Override
  public ValueWrapper putIfAbsent(Object key, Object value) {
    return toValueWrapper(false);
  }

  @Override
  public void evict(Object key) {

  }

  @Override
  public void clear() {

  }
}
