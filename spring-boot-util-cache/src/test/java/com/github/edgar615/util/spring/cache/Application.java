package com.github.edgar615.util.spring.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.github.edgar615.**"})//扫描jar
@EnableCaching
@RestController
@Configuration
public class Application {

  @Autowired
  private CacheService cacheService;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}