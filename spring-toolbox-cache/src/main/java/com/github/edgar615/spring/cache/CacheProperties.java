package com.github.edgar615.spring.cache;

import java.util.HashMap;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix = "caching")
public class CacheProperties {

  private Redis redis = new Redis();

  private Caffeine caffeine = new Caffeine();

  private L2Cache l2Cache = new L2Cache();

  public Redis getRedis() {
    return redis;
  }

  public void setRedis(Redis redis) {
    this.redis = redis;
  }

  public Caffeine getCaffeine() {
    return caffeine;
  }

  public void setCaffeine(Caffeine caffeine) {
    this.caffeine = caffeine;
  }

  public L2Cache getL2Cache() {
    return l2Cache;
  }

  public void setL2Cache(L2Cache l2Cache) {
    this.l2Cache = l2Cache;
  }

  public static class Caffeine {

    private Map<String, String> spec = new HashMap<>();

    public Map<String, String> getSpec() {
      return spec;
    }

    public void setSpec(Map<String, String> spec) {
      this.spec = spec;
    }
  }

  public static class Redis {
    private Map<String, RedisCacheSpec> spec = new HashMap<>();

    public Map<String, RedisCacheSpec> getSpec() {
      return spec;
    }

    public void setSpec(
            Map<String, RedisCacheSpec> spec) {
      this.spec = spec;
    }
  }

  public static class L2Cache {
    private Map<String, L2CacheSpec> spec = new HashMap<>();

    public Map<String, L2CacheSpec> getSpec() {
      return spec;
    }

    public void setSpec(
            Map<String, L2CacheSpec> spec) {
      this.spec = spec;
    }
  }
}
