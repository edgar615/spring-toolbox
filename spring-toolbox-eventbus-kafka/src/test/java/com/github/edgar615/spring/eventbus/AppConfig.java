package com.github.edgar615.spring.eventbus;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.eventbus.EventProducer;
import com.github.edgar615.util.eventbus.ProducerStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
@Configuration
public class AppConfig {

//  @Bean
//  EventProducer eventProducer() {
//    return new EventProducer() {
//      @Override
//      public void send(Event event) {
//
//      }
//    };
//  }

//  @Bean
//  ProducerStorage storage() {
//    return new ProducerStorage() {
//      @Override
//      public boolean shouldStorage(Event event) {
//        return false;
//      }
//
//      @Override
//      public void save(Event event) {
//
//      }
//
//      @Override
//      public List<Event> pendingList() {
//        return null;
//      }
//
//      @Override
//      public void mark(Event event, int status) {
//
//      }
//    };
//  }
}
