package com.github.edgar615.spring.binlog;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix = "startcache.binlog")
public class BinlogProperties {

  /**
   * slave的用户名
   */
  private String username;

  /**
   * slave的密码
   */
  private String password;

  /**
   * 要缓存的表
   */
  private List<String> tables = new ArrayList<>();

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

  public List<String> getTables() {
    return tables;
  }

  public void setTables(List<String> tables) {
    this.tables = tables;
  }
}
