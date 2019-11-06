package com.github.edgar615.spring.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisDistributedLock extends AbstractDistributedLock {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisDistributedLock.class);

  private final RedisTemplate<String, String> redisTemplate;

  protected RedisDistributedLock(RedisTemplate<String, String> redisTemplate, String lockKey,
      String lockValue, long expireMills) {
    super(lockKey, lockValue, expireMills);
    this.redisTemplate = redisTemplate;
  }

  @Override
  public boolean acquire() {
    return false;
  }

  @Override
  public boolean release() {
    return false;
  }
}
