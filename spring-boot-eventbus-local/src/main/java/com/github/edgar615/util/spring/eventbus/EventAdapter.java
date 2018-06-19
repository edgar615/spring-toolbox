package com.github.edgar615.util.spring.eventbus;

import com.github.edgar615.util.event.Event;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Edgar on 2017/10/24.
 *
 * @author Edgar  Date 2017/10/24
 */
public class EventAdapter extends ApplicationEvent {
  private EventAdapter(Event source) {
    super(source);
  }

  public static EventAdapter create(Event event) {
    return new EventAdapter(event);
  }
}
