package com.github.edgar615.util.spring.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

  /**
   * 缓存的定义
   */
  private List<String> spec = new ArrayList<>();

  /**
   * 动态生成缓存名称的定义
   */
  private List<String> dynamic = new ArrayList<>();

  public List<String> getSpec() {
    return spec;
  }

  public void setSpec(List<String> spec) {
    this.spec = spec;
  }

  public List<String> getDynamic() {
    return dynamic;
  }

  public void setDynamic(List<String> dynamic) {
    this.dynamic = dynamic;
  }
}
