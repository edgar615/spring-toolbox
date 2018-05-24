package com.github.edgar615.util.spring.eventbus;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.eventbus.EventProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@SpringBootApplication(scanBasePackages = {"com.github.edgar615.**"})//扫描jar
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}