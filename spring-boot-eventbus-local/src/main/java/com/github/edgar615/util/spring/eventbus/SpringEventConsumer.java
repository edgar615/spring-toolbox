package com.github.edgar615.util.spring.eventbus;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.eventbus.EventConsumer;
import com.github.edgar615.util.eventbus.EventHandler;
import com.github.edgar615.util.eventbus.HandlerRegistration;
import com.github.edgar615.util.eventbus.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

@Service
public class SpringEventConsumer implements ApplicationListener<EventAdapter>, EventConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpringEventConsumer.class);

  @Override
  @Async
  public void onApplicationEvent(EventAdapter eventAdapter) {
    Event event = (Event) eventAdapter.getSource();
    try {
      LOGGER.info("<====== [{}] [{}] [{}] [{}] [{}]",
                  event.head().id(),
                  event.head().to(),
                  event.head().action(),
                  Helper.toHeadString(event),
                  Helper.toActionString(event));
      List<EventHandler> handlers =
              HandlerRegistration.instance()
                      .getHandlers(event);
      if (handlers == null || handlers.isEmpty()) {
        LOGGER.info("---| [{}] [NO HANDLER]", event.head().id());
      } else {
        for (EventHandler handler : handlers) {
          handler.handle(event);
        }
      }
    } catch (Exception e) {
      LOGGER.error("---| [{}] [Failed]", event.head().id(), e);
    }
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void close() {

  }

  @Override
  public Map<String, Object> metrics() {
    return null;
  }

  @Override
  public void consumer(BiPredicate<String, String> predicate, EventHandler handler) {
    HandlerRegistration.instance().registerHandler(predicate, handler);
  }

  @Override
  public void consumer(String topic, String resource, EventHandler handler) {
    final BiPredicate<String, String> predicate = (t, r) -> {
      boolean topicMatch = true;
      if (topic != null) {
        topicMatch = topic.equals(t);
      }
      boolean resourceMatch = true;
      if (resource != null) {
        resourceMatch = resource.equals(r);
      }
      return topicMatch && resourceMatch;
    };
    consumer(predicate, handler);
  }

  @Override
  public long waitForHandle() {
    return 0;
  }
}
