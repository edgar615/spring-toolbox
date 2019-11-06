package com.github.edgar615.spring.binlog;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Edgar on 2017/10/24.
 *
 * @author Edgar  Date 2017/10/24
 */
public class DbDataChangedEvent extends ApplicationEvent {
  private DbDataChangedEvent(DbChangedData source) {
    super(source);
  }

  public static DbDataChangedEvent create(DbChangedData data) {
    return new DbDataChangedEvent(data);
  }
}
