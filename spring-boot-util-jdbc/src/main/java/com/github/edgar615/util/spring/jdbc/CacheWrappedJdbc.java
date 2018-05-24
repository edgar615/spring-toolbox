package com.github.edgar615.util.spring.jdbc;

import com.google.common.collect.Lists;

import com.github.edgar615.util.db.Jdbc;
import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.search.Example;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/5/24.
 *
 * @author Edgar  Date 2018/5/24
 */
public class CacheWrappedJdbc implements Jdbc {
  private final Jdbc jdbc;

  private final CacheManager cacheManager;

  public CacheWrappedJdbc(Jdbc jdbc, CacheManager cacheManager) {
    this.jdbc = jdbc;
    this.cacheManager = cacheManager;
  }

  @Override
  public <ID> void insert(Persistent<ID> persistent) {
    jdbc.insert(persistent);
  }

  @Override
  public <ID> void insertAndGeneratedKey(Persistent<ID> persistent) {
    jdbc.insertAndGeneratedKey(persistent);
  }

  @Override
  public <ID, T extends Persistent<ID>> int deleteById(Class<T> elementType, ID id) {
    int result = jdbc.deleteById(elementType, id);
    evictCache(elementType, id);
    return result;
  }

  @Override
  public <ID, T extends Persistent<ID>> int deleteByExample(Class<T> elementType, Example example) {
    int result = jdbc.deleteByExample(elementType, example);
    clearCache(elementType);
    return result;
  }

  @Override
  public <ID> int updateById(Persistent<ID> persistent, Map<String, Integer> addOrSub,
                             List<String> nullFields, ID id) {
    int result = jdbc.updateById(persistent, addOrSub, nullFields, id);
    evictCache(persistent.getClass(), id);
    return result;
  }

  @Override
  public <ID> int updateByExample(Persistent<ID> persistent, Map<String, Integer> addOrSub,
                                  List<String> nullFields, Example example) {
    int result = jdbc.updateByExample(persistent, addOrSub, nullFields, example);
    clearCache(persistent.getClass());
    return result;
  }

  @Override
  public <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id,
                                                   List<String> fields) {
    Cache cache = cacheManager.getCache(cacheName(elementType));
    if (cache == null) {
      return jdbc.findById(elementType, id, fields);
    }
    T persistent = null;
    Cache.ValueWrapper valueWrapper = cache.get(id);
    if (valueWrapper != null) {
      persistent = (T) valueWrapper.get();
    } else {
      persistent = jdbc.findById(elementType, id, Lists.newArrayList());
      if (persistent == null) {
        return null;
      }
      cache.put(id, persistent);
    }
    if (fields == null || fields.isEmpty()) {
      return persistent;
    }
    Map<String, Object> map = persistent.toMap();
    fields.stream().forEach(f -> map.remove(f));
    Persistent<ID> newPersistent = newDomain(elementType);
    newPersistent.fromMap(map);
    return (T) newPersistent;
  }

  @Override
  public <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType,
                                                              Example example) {
    return jdbc.findByExample(elementType, example);
  }

  @Override
  public <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType, Example example,
                                                              int start, int limit) {
    return jdbc.findByExample(elementType, example, start, limit);
  }

  @Override
  public <ID, T extends Persistent<ID>> int countByExample(Class<T> elementType, Example example) {
    return jdbc.countByExample(elementType, example);
  }

  private void evictCache(Class elementType, Object id) {
    Cache cache = cacheManager.getCache(cacheName(elementType));
    if (cache != null) {
      cache.evict(id);
    }
  }

  private void clearCache(Class elementType) {
    Cache cache = cacheManager.getCache(cacheName(elementType));
    if (cache != null) {
      cache.clear();
    }
  }

  private <ID> Persistent newDomain(Class<? extends Persistent<ID>> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String cacheName(Class elementType) {
    return elementType.getSimpleName() + "JdbcCache";
  }
}
