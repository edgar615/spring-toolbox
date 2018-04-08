package com.github.edgar615.util.spring.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar on 2017/10/9.
 *
 * @author Edgar  Date 2017/10/9
 */
@ConfigurationProperties(prefix = "system.log")
public class LogProperty {
  private boolean enabled;

  private List<String> ignore = new ArrayList<>();

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public List<String> getIgnore() {
    return ignore;
  }

  public void setIgnore(List<String> ignore) {
    this.ignore = ignore;
  }
}
