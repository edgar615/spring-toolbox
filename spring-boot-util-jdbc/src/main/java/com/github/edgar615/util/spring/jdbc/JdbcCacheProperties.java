package com.github.edgar615.util.spring.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jdbc.caching")
public class JdbcCacheProperties {

  private Map<String, Map<String, List<TableCacheSpec>>> config;

  public Map<String, Map<String, List<TableCacheSpec>>> getConfig() {
    return config;
  }

  public void setConfig(
      Map<String, Map<String, List<TableCacheSpec>>> config) {
    this.config = config;
  }
//  public Map<String, List<TableCacheSpec>> tableCacheSpec() {
//    if (config == null) {
//      return null;
//    }
//    Map<String, List<TableCacheSpec>> stringMap = new HashMap<>();
//    for (String key : config.keySet()) {
////     stringMap.put(key, )
//    }
//  }
//
//  private TableCacheSpec transformToSpec(String config) {
////    List<String> config
//  }
}
