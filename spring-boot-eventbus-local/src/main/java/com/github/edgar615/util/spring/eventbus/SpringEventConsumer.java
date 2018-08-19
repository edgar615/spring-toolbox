package com.github.edgar615.util.spring.eventbus;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.eventbus.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public class SpringEventConsumer implements ApplicationListener<EventAdapter>, EventConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpringEventConsumer.class);

  @Override
  @Async
  public void onApplicationEvent(EventAdapter eventAdapter) {
    Event event = (Event) eventAdapter.getSource();
    EventIdTracing eventIdTracing = new EventIdTracing(event.head().id());
    EventIdTracingHolder.set(eventIdTracing);
    MDC.put("x-request-id", event.head().id());
    long start = System.currentTimeMillis();
    try {
      LOGGER.info("[{}] [MR] [LOCAL] [{}] [{}] [{}]", event.head().id(), event.head().to(),
              Helper.toHeadString(event),
              Helper.toActionString(event));
      List<EventHandler> handlers =
              HandlerRegistration.instance()
                      .getHandlers(event);
      if (handlers == null || handlers.isEmpty()) {
        LOGGER.warn("[{}] [EC] [no handler]", event.head().id());
      } else {
        for (EventHandler handler : handlers) {
          handler.handle(event);
        }
        LOGGER.info("[{}] [EC] [OK] [{}ms]", event.head().id(), System.currentTimeMillis() - start);
      }
    } catch (Exception e) {
      LOGGER.error("[{}] [EC] [failed]", event.head().id(), e);
    } finally {
      EventIdTracingHolder.clear();
      MDC.remove("x-request-id");
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

  @Override
  public boolean paused() {
    return false;
  }

  @Override
  public boolean isRunning() {
    return true;
  }
}
