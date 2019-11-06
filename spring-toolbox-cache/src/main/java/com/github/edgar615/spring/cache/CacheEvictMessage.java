package com.github.edgar615.spring.cache;

import java.util.Objects;

/**
 * Created by Edgar on 2018/8/17.
 *
 * @author Edgar  Date 2018/8/17
 */
public class CacheEvictMessage {
  private final String cacheName;

  private final Object key;

  private final boolean allEntries;

  private CacheEvictMessage(String cacheName, Object key, boolean allEntries) {
    this.cacheName = cacheName;
    this.key = key;
    this.allEntries = allEntries;
  }

  /**
   * 淘汰缓存中的某个特定值
   *
   * @param cacheName
   * @param key
   * @return
   */
  public static CacheEvictMessage withKey(String cacheName, Object key) {
    Objects.requireNonNull(cacheName);
    Objects.requireNonNull(key);
    return new CacheEvictMessage(cacheName, key, false);
  }

  /**
   * 清空缓存
   *
   * @param cacheName
   * @return
   */
  public static CacheEvictMessage allEntries(String cacheName) {
    Objects.requireNonNull(cacheName);
    return new CacheEvictMessage(cacheName, null, true);
  }

  public String cacheName() {
    return cacheName;
  }

  public Object key() {
    return key;
  }

  public boolean allEntries() {
    return allEntries;
  }
}
