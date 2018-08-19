package com.github.edgar615.util.spring.eventbus;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.Message;
import com.github.edgar615.util.eventbus.EventProducer;
import com.google.common.collect.ImmutableMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = {"com.github.edgar615.**"})
@Configuration
@EnableLocalEventBus
public class Application {
  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(Application.class, args);
    EventProducer producer = context.getBean(EventProducer.class);
    Message message = Message.create("test", ImmutableMap.of("foo", "bar"));
    Event event = Event.create("DeviceControlEvent_1_3", message);
    producer.send(event);

    event = Event.create("test", message);
    producer.send(event);
  }
}
