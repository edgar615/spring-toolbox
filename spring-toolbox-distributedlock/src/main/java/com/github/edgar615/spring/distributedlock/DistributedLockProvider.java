package com.github.edgar615.spring.distributedlock;

public interface DistributedLockProvider {

  /**
   * 获取锁
   *
   * @param distributedLock 锁
   * @return true表示获得锁
   */
  boolean acquire(DistributedLock distributedLock);

  /**
   * 刷新锁的过期时间，有些任务可能会阻塞，导致锁被提前释放，引起锁的安全性问题。
   *
   * 可以通过定时任务调用hold方法将阻塞方法的过期时间延长，但是这样会导致有的锁可能被长期占用
   *
   * @param distributedLock 锁
   */
  void hold(DistributedLock distributedLock);

  /**
   * 释放锁
   *
   * @param storeName 锁的存储空间
   * @param lockKey 锁的资源
   * @param lockValue 锁的客户端标识
   * @return true表示释放锁成功
   */
  boolean release(String storeName, String lockKey, String lockValue);

}
