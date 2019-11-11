package com.github.edgar615.spring.cache;

import org.springframework.context.ApplicationEvent;

public class StartCacheChangedEvent extends ApplicationEvent {

  /**
   * Create a new ApplicationEvent.
   *
   * @param source the object on which the event initially occurred (never {@code null})
   */
  public StartCacheChangedEvent(Object source) {
    super(source);
  }
}
