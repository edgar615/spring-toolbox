package com.github.edgar615.util.spring.auth;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.ArrayList;
import java.util.List;

public class AuthUtils {

  public static void addSimpleAuthInterceptors(InterceptorRegistry registry,
                                               SimpleAuthInterceptor interceptor,
                                               AuthProperties authProperties,
                                               int order) {
    String[] patterns = patterns(authProperties);
    registry.addInterceptor(interceptor).addPathPatterns("/**")
            .excludePathPatterns(patterns).order(order);
  }

  private static String[] patterns(AuthProperties authProperties) {List<String>
          excludePathPatternsForAuth = new ArrayList<>();
    if (authProperties.getIgnore() != null) {
      excludePathPatternsForAuth.addAll(authProperties.getIgnore());
    }
    excludePathPatternsForAuth.add("/error");
    return excludePathPatternsForAuth.toArray(new String[excludePathPatternsForAuth.size()]);
  }

  public static void addAuthInterceptors(InterceptorRegistry registry, AuthInterceptor interceptor,
                                         AuthProperties authProperties,
                                         int order) {
    String[] patterns = patterns(authProperties);
    registry.addInterceptor(interceptor).addPathPatterns("/**")
            .excludePathPatterns(patterns).order(order);
  }
}
