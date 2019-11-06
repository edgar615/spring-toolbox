package com.github.edgar615.spring.jwt;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/4/22.
 */
@ConfigurationProperties(prefix = "jwt.interceptor")
@Service
public class JwtInterceptorProperties {

  private List<String> ignoreUrl;

  public List<String> getIgnoreUrl() {
    return ignoreUrl;
  }

  public void setIgnoreUrl(List<String> ignoreUrl) {
    this.ignoreUrl = ignoreUrl;
  }
}
