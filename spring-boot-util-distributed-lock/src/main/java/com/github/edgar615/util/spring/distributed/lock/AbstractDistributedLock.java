package com.github.edgar615.util.spring.distributed.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDistributedLock implements DistributedLock {

  private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLock.class);

  /**
   * redis key
   */
  private final String lockKey;

  /**
   * redis value
   */
  private final String lockValue;

  /**
   * 过期时间，单位毫秒
   */
  private final long expireMills;

  /**
   * 重试间隔，默认
   */
  private long retryIntervalMills = 200L;

  /**
   * 重试次数，默认不重试
   */
  private int retryTimes;


  protected AbstractDistributedLock(String lockKey, String lockValue, long expireMills) {
    this.lockKey = lockKey;
    this.lockValue = lockValue;
    this.expireMills = expireMills;
  }

  public String lockKey() {
    return lockKey;
  }

  public String lockValue() {
    return lockValue;
  }

  public long expireMills() {
    return expireMills;
  }

  public long retryIntervalMills() {
    return retryIntervalMills;
  }


  public int retryTimes() {
    return retryTimes;
  }

  public AbstractDistributedLock setRetryIntervalMills(long retryIntervalMills) {
    this.retryIntervalMills = retryIntervalMills;
    return this;
  }

  public AbstractDistributedLock setRetryTimes(int retryTimes) {
    this.retryTimes = retryTimes;
    return this;
  }
}
