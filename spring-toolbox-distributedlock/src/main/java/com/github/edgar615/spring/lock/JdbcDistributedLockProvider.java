package com.github.edgar615.spring.lock;

import org.springframework.stereotype.Service;

@Service
public class JdbcDistributedLockProvider implements DistributedLockProvider {

  public static final String ACQUIRE_FORMATTED_QUERY = "INSERT INTO %s (lock_key, token, expireAt) VALUES (?, ?, ?);";
  public static final String RELEASE_FORMATTED_QUERY = "DELETE FROM %s WHERE lock_key = ? AND token = ?;";
  public static final String DELETE_EXPIRED_FORMATTED_QUERY = "DELETE FROM %s WHERE expireAt < ?;";
  public static final String REFRESH_FORMATTED_QUERY = "UPDATE %s SET expireAt = ? WHERE lock_key = ? AND token = ?;";

  @Override
  public boolean acquire(AbstractDistributedLock distributedLock) {
    return false;
  }

  @Override
  public void hold(AbstractDistributedLock distributedLock) {

  }

  @Override
  public boolean release(AbstractDistributedLock distributedLock) {
    return false;
  }
}
