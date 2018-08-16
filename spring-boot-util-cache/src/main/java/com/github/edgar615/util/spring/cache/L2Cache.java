package com.github.edgar615.util.spring.cache;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.hash.PrimitiveSink;
import com.google.common.util.concurrent.Striped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * 二级缓存.
 *
 * @author Edgar  Date 2018/8/15
 */
public class L2Cache extends AbstractValueAdaptingCache {

  private final Logger logger = LoggerFactory.getLogger(L2Cache.class);

  private final Cache level1;

  private final Cache level2;

  private final String name;

  private final Striped<ReadWriteLock> striped;

  public L2Cache(String name, Cache level1, Cache level2, boolean allowNullValues) {
    super(allowNullValues);
    this.level1 = level1;
    this.level2 = level2;
    this.name = name;
    this.striped = Striped.lazyWeakReadWriteLock(64);
  }


  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Object getNativeCache() {
    return this;
  }

  @Override
  public <T> T get(Object key, Callable<T> valueLoader) {
    //因为sharedData内部保存lock的map无法删除lock，如果key比较多，对内存的占用会一直得不到释放。因此这里使用hash来限制lock的数量.
    HashCode hashCode = Hashing.md5().newHasher().putObject(key, new Funnel<Object>() {
      @Override
      public void funnel(Object from, PrimitiveSink into) {
        into.putString(from.toString(), Charsets.UTF_8);
      }
    }).hash();
    int hash = Hashing.consistentHash(hashCode, 64);
    ReadWriteLock lock = striped.get(hash);
    Lock rl = lock.readLock();
    Lock wl = lock.writeLock();
    Object value = null;
    try {
      rl.lock();
      value = lookup(key);
    } finally {
      rl.unlock();
    }
    if (value != null) {
      return (T) fromStoreValue(value);
    }
    try {
      wl.lock();
      value = lookup(key);
      if (value == null) {
        value = valueLoader.call();
        put(key, value);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      wl.unlock();
    }
    return (T) fromStoreValue(value);
  }

  @Override
  public void put(Object key, @Nullable Object value) {
    level2.put(key, toStoreValue(value));
    level1.put(key, toStoreValue(value));
  }

  @Override
  public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
    ValueWrapper valueWrapper = level2.putIfAbsent(key, value);
    if (valueWrapper != null) {
      return valueWrapper;
    }
    valueWrapper = level1.putIfAbsent(key, value);
    if (valueWrapper != null) {
      return valueWrapper;
    }
    return null;
  }

  @Override
  public void evict(Object key) {
    // 先清除level2中缓存数据，然后清除level1中的缓存，避免短时间内如果先清除level1缓存后其他请求会再从level2里加载到level2中
    level2.evict(key);
    level1.evict(key);
  }

  @Override
  public void clear() {
// 先清除level2中缓存数据，然后清除level1中的缓存，避免短时间内如果先清除level1缓存后其他请求会再从level2里加载到level2中
    level2.clear();
    level1.clear();
  }

  @Override
  protected Object lookup(Object key) {
    Object value = level1.get(key);
    if (value != null) {
      logger.debug("get cache from level1, the key is {}", key);
      if (value instanceof ValueWrapper) {
        return ((ValueWrapper) value).get();
      }
      return value;
    }

    value = level2.get(key);

    if (value != null) {
      logger.debug("get cache from level2, the key is {}", key);
      if (value instanceof ValueWrapper) {
        level1.put(key, ((ValueWrapper) value).get());
        return ((ValueWrapper) value).get();
      }
      return value;

    }
    return value;
  }
}
