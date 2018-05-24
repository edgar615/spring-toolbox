package com.github.edgar615.util.spring.eventbus;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.Message;
import com.github.edgar615.util.eventbus.EventConsumer;
import com.github.edgar615.util.eventbus.EventProducer;
import com.github.edgar615.util.eventbus.KafkaEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Edgar on 2017/10/17.
 *
 * @author Edgar  Date 2017/10/17
 */
@Service
public class ProducerTest {

//  @Autowired
//  private EventProducer producer;
//
//  @PostConstruct
//  public void producer() {
//    for (int i = 0; i < 1000; i++) {
//      System.out.println(i);
//      Message message = Message.create("" + i, ImmutableMap.of("foo", "bar"));
//      Event event = Event.create("DeviceControlEvent_1_3", message);
//      producer.send(event);
//    }
//  }
}
