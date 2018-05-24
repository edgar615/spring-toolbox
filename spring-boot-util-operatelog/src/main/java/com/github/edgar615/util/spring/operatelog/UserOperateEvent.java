package com.github.edgar615.util.spring.operatelog;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Edgar on 2018/5/24.
 *
 * @author Edgar  Date 2018/5/24
 */
public class UserOperateEvent extends ApplicationEvent {

  /**
   * Create a new ApplicationEvent.
   *
   * @param source the object on which the event initially occurred (never {@code null})
   */
  public UserOperateEvent(OperateLog source) {
    super(source);
  }

}
