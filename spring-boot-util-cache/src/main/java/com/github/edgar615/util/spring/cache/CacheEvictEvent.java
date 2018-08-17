package com.github.edgar615.util.spring.cache;

import org.springframework.context.ApplicationEvent;

/**
 * 接受spring event，用于刷新驱逐缓存.
 * 在有些前后台分离、服务分离等场景下，JVM的本地内存有时候会出现数据不一致问题。
 * 为了解决这个问题，我们需要在数据发生变化时将缓存淘汰。通过增加一次查找的方式达到最终要一致性。
 *
 * @author Edgar  Date 2018/8/17
 */
public class CacheEvictEvent extends ApplicationEvent {

  private CacheEvictMessage message;
  /**
   * Create a new ApplicationEvent.
   *
   * @param message the object on which the event initially occurred (never {@code null})
   */
  public CacheEvictEvent(CacheEvictMessage message) {
    super(message);
  }
}
