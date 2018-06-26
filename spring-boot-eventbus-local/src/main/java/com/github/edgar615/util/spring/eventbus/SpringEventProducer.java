package com.github.edgar615.util.spring.eventbus;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.eventbus.EventProducer;
import com.github.edgar615.util.eventbus.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Edgar on 2017/10/24.
 *
 * @author Edgar  Date 2017/10/24
 */
public class SpringEventProducer implements EventProducer, ApplicationEventPublisherAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpringEventProducer.class);

  private ApplicationEventPublisher publisher;

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.publisher = applicationEventPublisher;
  }

  @Override
  public void send(Event event) {
    try {
      publisher.publishEvent(EventAdapter.create(event));
      LOGGER.info("======> [{}] [OK] [{}] [{}] [{}] [{}]",
                  event.head().id(),
                  event.head().to(),
                  event.head().action(),
                  Helper.toHeadString(event),
                  Helper.toActionString(event));
    } catch (Exception e) {
      LOGGER.info("======> [{}] [FAILED] [{}] [{}] [{}] [{}]",
                  event.head().id(),
                  event.head().to(),
                  event.head().action(),
                  Helper.toHeadString(event),
                  Helper.toActionString(event));
    }

  }

  @Override
  public void close() {

  }

  @Override
  public Map<String, Object> metrics() {
    return null;
  }

  @Override
  public long waitForSend() {
    return 0;
  }
}
