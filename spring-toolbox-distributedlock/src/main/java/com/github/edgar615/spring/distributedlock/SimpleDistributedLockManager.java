package com.github.edgar615.spring.distributedlock;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class SimpleDistributedLockManager implements DistributedLockManager {

  private final DistributedLockProvider provider;

  private volatile Set<String> cacheNames = Collections.emptySet();

  public SimpleDistributedLockManager(
      DistributedLockProvider provider) {
    this.provider = provider;
  }

  @Override
  public Collection<String> getStoreNames() {
    return null;
  }

  @Override
  public boolean acquire(DistributedLock distributedLock) {
    return provider.acquire(distributedLock);
  }

  @Override
  public void hold(DistributedLock distributedLock) {
    provider.hold(distributedLock);
  }

  @Override
  public boolean release(String storeName, String lockKey, String lockValue) {
    return provider.release(storeName, lockKey, lockValue);
  }
}
