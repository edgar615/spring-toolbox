package com.github.edgar615.spring.cache;

import com.github.edgar615.spring.cache.refresh.CachingAnnotationsRefreshAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.github.edgar615.**"})//扫描jar
@EnableCaching
@EnableAspectJAutoProxy
@RestController
@Configuration
public class Application {

  @Autowired
  private CacheService cacheService;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CachingAnnotationsRefreshAspect cachingAnnotationsRefreshAspect() {
    return new CachingAnnotationsRefreshAspect(new SimpleKeyGenerator());
  }

}
