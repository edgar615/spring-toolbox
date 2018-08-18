package com.github.edgar615.util.spring.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix="system")
public class SystemProperty {

  private Map<String, String> errorPage = new HashMap<>();

  public Map<String, String> getErrorPage() {
    return errorPage;
  }

  public void setErrorPage(Map<String, String> errorPage) {
    this.errorPage = errorPage;
  }
}
