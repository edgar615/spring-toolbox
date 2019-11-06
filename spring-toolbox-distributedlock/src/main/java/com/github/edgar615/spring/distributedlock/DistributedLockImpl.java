package com.github.edgar615.spring.distributedlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributedLockImpl implements DistributedLock {

  private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLock.class);

  /**
   * 锁的存储空间，对于redis来说，这个就是redis key的前缀，对于数据库来说这个值是数据库表名
   */
  private final String storeName;

  /**
   * 锁的资源。例如锁定某个订单:order:#orderno，锁定整个任务表job
   */
  private final String lockKey;

  /**
   * 锁的客户端标识
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


  public DistributedLockImpl(String storeName, String lockKey, String lockValue,
      long expireMills) {
    this.storeName = storeName;
    this.lockKey = lockKey;
    this.lockValue = lockValue;
    this.expireMills = expireMills;
  }

  @Override
  public String storeName() {
    return storeName;
  }

  @Override
  public String lockKey() {
    return lockKey;
  }

  @Override
  public String lockValue() {
    return lockValue;
  }

  @Override
  public long expireMills() {
    return expireMills;
  }

  @Override
  public long retryIntervalMills() {
    return retryIntervalMills;
  }

  @Override
  public int retryTimes() {
    return retryTimes;
  }

  public DistributedLockImpl setRetryIntervalMills(long retryIntervalMills) {
    this.retryIntervalMills = retryIntervalMills;
    return this;
  }

  public DistributedLockImpl setRetryTimes(int retryTimes) {
    this.retryTimes = retryTimes;
    return this;
  }
}
