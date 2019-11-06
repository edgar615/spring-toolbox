package com.github.edgar615.spring.distributedlock.redis;

import com.github.edgar615.spring.distributedlock.DistributedLock;
import com.github.edgar615.spring.distributedlock.DistributedLockProvider;
import java.util.concurrent.TimeUnit;
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
 * Created on Sep 18, 2018
 *
 * @author Chuan Qin
 */
public class RedistDistributedLockProvider implements DistributedLockProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedistDistributedLockProvider.class);

  private RedisTemplate<String, String> redisTemplate;
  private DefaultRedisScript<Boolean> holdScript;
  private DefaultRedisScript<Boolean> releaseScript;

  public RedistDistributedLockProvider(RedisTemplate<String, String> redisTemplate) {
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
//        long expireInMillis;
//        if (TimeUnit.MILLISECONDS == timeUnit) {
//            expireInMillis = expire;
//        } else {
//            expireInMillis = timeUnit.toMillis(expire);
//        }
//        long delay = expireInMillis / 3;
//        final String lockValue = value.get();
//        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
//        exec.scheduleWithFixedDelay(() -> {
//            boolean result = refreshExpire(lockKey, lockValue, expireInMillis);
//            if (!result) {
//                LOGGER.debug("Failed to refresh expiration of lockKey, shutdown!");
//                exec.shutdown();
//            }
//        }, delay, delay, TimeUnit.MILLISECONDS);
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
          Expiration.from(distributedLock.expireMills(), TimeUnit.MILLISECONDS),
          SetOption.SET_IF_ABSENT);
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
