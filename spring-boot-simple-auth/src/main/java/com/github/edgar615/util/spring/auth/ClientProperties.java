package com.github.edgar615.util.spring.auth;

import java.util.List;

/**
 * Created by Edgar on 2017/11/16.
 *
 * @author Edgar  Date 2017/11/16
 */
public class ClientProperties {
  private List<String> ignore;

  public List<String> getIgnore() {
    return ignore;
  }

  public void setIgnore(List<String> ignore) {
    this.ignore = ignore;
  }
}
