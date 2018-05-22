package com.github.edgar615.util.spring.cache.binlog;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

  /**
   * jdbc url
   */
  private String url;

  /**
   * 用户名
   */
  private String username;

  /**
   * 密码
   */
  private String password;


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
