package com.github.edgar615.util.spring.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jdbc.caching")
public class JdbcCacheProperties {

  private List<String> defaultCacheTables;

  private JdbcCacheConfig config = new JdbcCacheConfig();

  public JdbcCacheConfig getConfig() {
    return config;
  }

  public void setConfig(JdbcCacheConfig config) {
    this.config = config;
  }

  public List<String> getDefaultCacheTables() {
    return defaultCacheTables;
  }

  public void setDefaultCacheTables(List<String> defaultCacheTables) {
    this.defaultCacheTables = defaultCacheTables;
  }

  public static class JdbcCacheConfig {

    private Map<String, JdbcCacheConfigSpec> customSpec = new HashMap<>();

    public Map<String, JdbcCacheConfigSpec> getCustomSpec() {
      return customSpec;
    }

    public void setCustomSpec(
        Map<String, JdbcCacheConfigSpec> customSpec) {
      this.customSpec = customSpec;
    }
  }

  public static class JdbcCacheConfigSpec {

    private String cacheNamePrefix;

    public String getCacheNamePrefix() {
      return cacheNamePrefix;
    }

    public void setCacheNamePrefix(String cacheNamePrefix) {
      this.cacheNamePrefix = cacheNamePrefix;
    }

  }
}
