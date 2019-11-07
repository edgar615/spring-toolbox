package com.github.edgar615.spring.distributedlock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class MockLocalLockProvier implements DistributedLockProvider {

  private final ConcurrentMap<String, String> registry = new ConcurrentHashMap<>();

  @Override
  public boolean acquire(DistributedLock distributedLock) {
    String value = registry.putIfAbsent(distributedLock.lockKey(), distributedLock.lockValue());
    return value == null;
  }

  @Override
  public void hold(DistributedLock distributedLock) {

  }

  @Override
  public boolean release(String storeName, String lockKey, String lockValue) {
    return registry.remove(lockKey, lockValue);
  }

  public int count() {
    return registry.size();
  }

  public void clear() {
    registry.clear();
  }
}
