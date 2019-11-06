package com.github.edgar615.spring.lock;

/**
 * Created on Sep 18, 2018
 *
 * @author Chuan Qin
 */
public interface DistributedLockProvider {

  boolean acquire(AbstractDistributedLock distributedLock);

  void hold(AbstractDistributedLock distributedLock);

  boolean release(AbstractDistributedLock distributedLock);

}
