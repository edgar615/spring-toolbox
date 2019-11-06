package com.github.edgar615.spring.eventbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.github.edgar615.**"})//扫描jar
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
