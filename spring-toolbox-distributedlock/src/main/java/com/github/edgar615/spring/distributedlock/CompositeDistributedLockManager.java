package com.github.edgar615.spring.distributedlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public class CompositeDistributedLockManager implements DistributedLockManager {

  private final List<DistributedLockManager> lockManagers = new ArrayList<>();

  @Override
  public Collection<String> getStoreNames() {
    Set<String> names = new LinkedHashSet<>();
    for (DistributedLockManager lockManager : this.lockManagers) {
      names.addAll(lockManager.getStoreNames());
    }
    return Collections.unmodifiableSet(names);
  }

  @Override
  public boolean acquire(DistributedLock distributedLock) {
//    for (DistributedLockManager lockManager : this.lockManagers) {
//      Cache cache = lockManager.getCache(name);
//      if (cache != null) {
//        return cache;
//      }
//    }

    return false;
  }

  @Override
  public void hold(DistributedLock distributedLock) {

  }

  @Override
  public boolean release(String storeName, String lockKey, String lockValue) {
    return false;
  }
}
