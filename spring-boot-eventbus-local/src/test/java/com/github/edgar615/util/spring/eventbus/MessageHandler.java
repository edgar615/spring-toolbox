package com.github.edgar615.util.spring.eventbus;

import com.github.edgar615.util.eventbus.EventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.naming.event.EventContext;

@Component
public class MessageHandler {

  @Autowired
  private EventConsumer consumer;

  @PostConstruct
  public void regHandler() {
    consumer.consumer("test", "test", e -> System.out.println(e));
  }
}
