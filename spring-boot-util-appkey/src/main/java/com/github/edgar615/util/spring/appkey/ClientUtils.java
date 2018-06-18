package com.github.edgar615.util.spring.appkey;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.ArrayList;
import java.util.List;

public class ClientUtils {
  public static void addSimpleClientInterceptors(InterceptorRegistry registry, SimpleClientInterceptor interceptor, ClientProperties clientProperties) {
    List<String> excludePathPatternsForAuth = new ArrayList<>();
    if (clientProperties.getIgnore() != null) {
      excludePathPatternsForAuth.addAll(clientProperties.getIgnore());
    }
    String[] patterns =
            excludePathPatternsForAuth.toArray(new String[excludePathPatternsForAuth.size()]);
    registry.addInterceptor(interceptor).addPathPatterns("/**")
            .excludePathPatterns(patterns);
  }

  public static void addClientInterceptors(InterceptorRegistry registry, ClientInfoInterceptor interceptor, ClientProperties clientProperties) {
    List<String> excludePathPatternsForAuth = new ArrayList<>();
    if (clientProperties.getIgnore() != null) {
      excludePathPatternsForAuth.addAll(clientProperties.getIgnore());
    }
    String[] patterns =
            excludePathPatternsForAuth.toArray(new String[excludePathPatternsForAuth.size()]);
    registry.addInterceptor(interceptor).addPathPatterns("/**")
            .excludePathPatterns(patterns);
  }

  public static void addFixClientInterceptors(InterceptorRegistry registry, FixClientInterceptor interceptor, ClientProperties clientProperties) {
    List<String> excludePathPatternsForAuth = new ArrayList<>();
    if (clientProperties.getIgnore() != null) {
      excludePathPatternsForAuth.addAll(clientProperties.getIgnore());
    }
    String[] patterns =
            excludePathPatternsForAuth.toArray(new String[excludePathPatternsForAuth.size()]);
    registry.addInterceptor(interceptor).addPathPatterns("/**")
            .excludePathPatterns(patterns);
  }

}
