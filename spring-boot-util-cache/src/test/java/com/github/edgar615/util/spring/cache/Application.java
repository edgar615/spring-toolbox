package com.github.edgar615.util.spring.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.github.edgar615.**"})//扫描jar
@EnableCaching
@RestController
public class Application {

  @Autowired
  private CacheService cacheService;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @RequestMapping(value = "/cache1", method = RequestMethod.GET)
  public String cache1(@RequestParam("id") int id) {
    return cacheService.getCache1(id);
  }

  @RequestMapping(value = "/cache2", method = RequestMethod.GET)
  public String cache2(@RequestParam("id") int id) {
    return cacheService.getCache2(id);
  }
}