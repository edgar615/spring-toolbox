package com.github.edgar615.util.spring.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationListener;

/**
 * 驱逐缓存事件监听器.
 *
 * @author Edgar  Date 2018/8/17
 */
public class CacheEvictEventListener implements ApplicationListener<CacheEvictEvent> {

  private final Logger logger = LoggerFactory.getLogger(CacheEvictEventListener.class);

  private final CacheManager cacheManager;

  public CacheEvictEventListener(CacheManager cacheManager) {this.cacheManager = cacheManager;}

  @Override
  public void onApplicationEvent(CacheEvictEvent event) {
    CacheEvictMessage message = (CacheEvictMessage) event.getSource();
    Cache cache = cacheManager.getCache(message.cacheName());
    if (cache == null) {
      logger.debug("no cache can be evict : {}", message.cacheName());
      return;
    }
    if (message.allEntries()) {
      logger.debug("clear cache: {}", message.cacheName());
      cache.clear();
    }
    if (message.key() != null) {
      logger.debug("evict cache: {}, {}", message.cacheName(), message.key());
      cache.evict(message.key());
    }
  }
}
