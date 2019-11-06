package com.github.edgar615.spring.web;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by Edgar on 2017/11/16.
 *
 * @author Edgar  Date 2017/11/16
 */
@ConfigurationProperties(prefix = "web")
@Service
public class WebProperties {

  private Map<String, InterceptorConfig> interceptors;

  public Map<String, InterceptorConfig> getInterceptors() {
    return interceptors;
  }

  public void setInterceptors(
      Map<String, InterceptorConfig> interceptors) {
    this.interceptors = interceptors;
  }
}
