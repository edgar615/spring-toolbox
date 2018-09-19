package com.github.edgar615.util.spring.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CacheServiceImpl implements CacheService, ApplicationEventPublisherAware {

  private ApplicationEventPublisher publisher;

  private final Map<String, AtomicInteger> stat = new HashMap<>();

  private void stat(String method) {
    if (stat.get(method) == null) {
      AtomicInteger integer = new AtomicInteger();
      integer.incrementAndGet();
      stat.put(method, integer);
    } else {
      stat.get(method).incrementAndGet();
    }
  }
  @Override
  @Cacheable(cacheNames = "caffeineCache1", key = "#p0")
  public String getCache1(int id) {
    System.out.println("cache1");
    stat("getCache1");
    return UUID.randomUUID().toString();
  }

  @Override
  @Cacheable(cacheNames = "caffeineCache2", key = "#p0")
  public String getCache2(int id) {
    System.out.println("cache2");
    stat("getCache2");
    return UUID.randomUUID().toString();
  }

  @Override
  @Cacheable(cacheNames = "l2Cache", key = "#p0")
  public String l2Cache(int id) {
    System.out.println("l2Cache");
    stat("l2Cache");
    return UUID.randomUUID().toString();
  }

  @Override
  public String clearCache1() {
    CacheEvictMessage message = CacheEvictMessage.allEntries("caffeineCache1");
    publisher.publishEvent(new CacheEvictEvent(message));
    return "clear1";
  }

  @Override
  public String evictCache1(int id) {
    CacheEvictMessage message = CacheEvictMessage.withKey("caffeineCache1", id);
    publisher.publishEvent(new CacheEvictEvent(message));
    return "evictCache1";
  }

  @Override
  public String clearL2Cache() {
    CacheEvictMessage message = CacheEvictMessage.allEntries("l2Cache");
    publisher.publishEvent(new CacheEvictEvent(message));
    return "clearL2Cache";
  }

  @Override
  public String evictL2Cache(int id) {
    CacheEvictMessage message = CacheEvictMessage.withKey("l2Cache", id);
    publisher.publishEvent(new CacheEvictEvent(message));
    return "evictL2Cache";
  }

  @Override
  public int count(String method) {
    return stat.getOrDefault(method, new AtomicInteger()).get();
  }

  @Override
  public void clearStat() {
    stat.clear();
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.publisher = applicationEventPublisher;
  }

  public Map<String, AtomicInteger> getStat() {
    return stat;
  }
}
