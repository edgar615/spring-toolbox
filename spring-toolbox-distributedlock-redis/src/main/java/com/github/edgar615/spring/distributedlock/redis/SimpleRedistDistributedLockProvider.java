package com.github.edgar615.spring.distributedlock.redis;

import com.github.edgar615.spring.distributedlock.DistributedLock;
import com.github.edgar615.spring.distributedlock.DistributedLockProvider;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.lettuce.LettuceConverters;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;

/**
 * 简易版的redis分布式锁实现，redis是单点
 */
public class SimpleRedistDistributedLockProvider implements DistributedLockProvider {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(SimpleRedistDistributedLockProvider.class);

  private RedisTemplate<String, String> redisTemplate;
  private DefaultRedisScript<Boolean> holdScript;
  private DefaultRedisScript<Boolean> releaseScript;

  public SimpleRedistDistributedLockProvider(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public boolean acquire(DistributedLock distributedLock) {
    boolean result = doAcquire(distributedLock);
    int retryTimes = distributedLock.retryTimes();
    // 如果获取锁失败，按照传入的重试次数进行重试
    while ((!result) && retryTimes-- > 0) {
      try {
        LOGGER.debug("Acquire failed, retrying.... lockKey={}, lockValue={}, retryTimes={}",
            distributedLock.lockKey(), distributedLock.lockValue(), retryTimes);
        Thread.sleep(distributedLock.retryIntervalMills());
      } catch (InterruptedException e) {
        return false;
      }
      result = doAcquire(distributedLock);
    }
    if (result) {
      LOGGER
          .debug("Acquire. lockKey={}, lockValue={}, expireInMillis={}", distributedLock.lockKey(),
              distributedLock.lockValue(), distributedLock.expireMills());
    } else {
      LOGGER.debug("Acquire failed. lockKey={}, lockValue={}", distributedLock.lockKey(),
          distributedLock.lockValue());
    }
    return result;
  }

  @Override
  public void hold(DistributedLock distributedLock) {
    refreshExpire(distributedLock.lockKey(), distributedLock.lockValue(),
        distributedLock.expireMills());
  }

  @Override
  public boolean release(String storeName, String lockKey, String lockValue) {
    return redisTemplate
        .execute((RedisCallback<Boolean>) connection -> connection.scriptingCommands()
            .eval(
                LettuceConverters.toBytes(releaseScript.getScriptAsString()),
                ReturnType.BOOLEAN,
                1,
                LettuceConverters.toBytes(lockKey),
                LettuceConverters.toBytes(lockValue)
            ));
  }

  @PostConstruct
  public void init() {
    holdScript = new DefaultRedisScript<>();
    holdScript.setLocation(new ClassPathResource("lua/hold.lua"));
    holdScript.setResultType(Boolean.class);
    releaseScript = new DefaultRedisScript<>();
    releaseScript.setLocation(new ClassPathResource("lua/release.lua"));
    releaseScript.setResultType(Boolean.class);
  }

  private boolean doAcquire(DistributedLock distributedLock) {
    return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
      //序列化key
      byte[] serializeKey = LettuceConverters.toBytes(distributedLock.lockKey());
      //序列化value
      byte[] serializeVal = LettuceConverters.toBytes(distributedLock.lockValue());
      boolean result = connection.stringCommands().set(serializeKey, serializeVal,
          Expiration.milliseconds(distributedLock.expireMills()), SetOption.SET_IF_ABSENT);
      return result;
    });
  }

  @SuppressWarnings("ConstantConditions")
  private boolean refreshExpire(String lockKey, String lockValue, long expireInMillis) {
    LOGGER.debug("lockKey={}, lockValue={}, expireInMillis={}", lockKey, lockValue, expireInMillis);
    boolean result = redisTemplate
        .execute((RedisCallback<Boolean>) connection -> connection.scriptingCommands()
            .eval(
                LettuceConverters.toBytes(holdScript.getScriptAsString()),
                ReturnType.BOOLEAN,
                1,
                LettuceConverters.toBytes(lockKey),
                LettuceConverters.toBytes(lockValue),
                LettuceConverters.toBytes(expireInMillis)
            ));

    if (LOGGER.isDebugEnabled()) {
      if (result) {
        LOGGER.debug("=========>>>>>>TTL refreshed!");
      } else {
        LOGGER.debug("=========>>>>>>Failed to refresh TTL!");
      }
    }

    return result;
  }
}
