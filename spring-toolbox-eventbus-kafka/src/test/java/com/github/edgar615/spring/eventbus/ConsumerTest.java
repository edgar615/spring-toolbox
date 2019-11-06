package com.github.edgar615.spring.eventbus;

import com.github.edgar615.util.eventbus.EventConsumer;
import com.github.edgar615.util.eventbus.KafkaEventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Edgar on 2017/10/17.
 *
 * @author Edgar  Date 2017/10/17
 */
@Service
public class ConsumerTest {

//  @Autowired
//  private EventConsumer consumer;
//
//  @PostConstruct
//  public void consumer() {
//    consumer.consumer(null, null, e -> {
//      System.out.println(e);
//    });
//  }
}
