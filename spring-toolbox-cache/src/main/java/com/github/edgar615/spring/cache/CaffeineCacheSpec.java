package com.github.edgar615.spring.cache;

public class CaffeineCacheSpec {
  private Long maximumSize;

  private Long expireAfterAccess;

  private Long expireAfterWrite;

  private Long refreshAfterWrite;

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
