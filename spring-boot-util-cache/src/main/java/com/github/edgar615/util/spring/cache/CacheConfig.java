package com.github.edgar615.util.spring.cache;

public class CacheConfig {
  private String type;

  private String name;

  private String spec;

  private Long maximumSize;

  private Long expireAfterAccess;

  private Long expireAfterWrite;

  private Long refreshAfterWrite;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSpec() {
    return spec;
  }

  public void setSpec(String spec) {
    this.spec = spec;
  }

  public Long getMaximumSize() {
    return maximumSize;
  }

  public void setMaximumSize(Long maximumSize) {
    this.maximumSize = maximumSize;
  }

  public Long getExpireAfterAccess() {
    return expireAfterAccess;
  }

  public void setExpireAfterAccess(Long expireAfterAccess) {
    this.expireAfterAccess = expireAfterAccess;
  }

  public Long getExpireAfterWrite() {
    return expireAfterWrite;
  }

  public void setExpireAfterWrite(Long expireAfterWrite) {
    this.expireAfterWrite = expireAfterWrite;
  }

  public Long getRefreshAfterWrite() {
    return refreshAfterWrite;
  }

  public void setRefreshAfterWrite(Long refreshAfterWrite) {
    this.refreshAfterWrite = refreshAfterWrite;
  }
}
