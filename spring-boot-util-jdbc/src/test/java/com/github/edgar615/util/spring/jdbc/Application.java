package com.github.edgar615.util.spring.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = {"com.github.edgar615.**"})
@Configuration
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}