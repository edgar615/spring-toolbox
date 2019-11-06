package com.github.edgar615.spring.distributedlock.jdbc;

import com.github.edgar615.spring.distributedlock.DistributedLock;
import com.github.edgar615.spring.distributedlock.DistributedLockProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务的隔离级别设置为读已提交，避免其他事务看不到已经提交的锁
 *
 * 这个锁并未解决锁重入问题，锁重入虽然可以增加lock_num来实现，但是实现更麻烦，如果有这种需求，建议时间用redis或者zk实现。
 *
 * 建表语句
 * <pre>
 * CREATE TABLE `distributed_lock` (
 * `id` BIGINT ( 20) NOT NULL AUTO_INCREMENT COMMENT '主键',
 * `lock_key` VARCHAR ( 64 ) NOT NULL COMMENT '锁定的资源',
 * `lock_value` VARCHAR ( 255 ) NOT NULL COMMENT '锁的客户端标识',
 * `locked_at` BIGINT ( 20 ) NOT NULL COMMENT '锁创建时间，单位毫秒',
 * `expire_at` BIGINT ( 20 ) NOT NULL COMMENT '锁过期时间，单位毫秒',
 * `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '保存数据时间，自动生成',
 * PRIMARY KEY ( `id` ),
 * UNIQUE KEY `uidx_lock_key` ( `lock_key` ) USING BTREE,
 * KEY `idx_expire_at` ( `expire_at` ) USING BTREE
 * ) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '分布式锁';
 * </pre>
 *
 */
@Service
public class SimpleJdbcDistributedLockProvider implements DistributedLockProvider {

  public static final String ACQUIRE_FORMATTED_QUERY = "INSERT INTO %s (lock_key, lock_value, locked_at, expire_at) VALUES (?, ?, ?, ?);";
  public static final String RELEASE_FORMATTED_QUERY = "DELETE FROM %s WHERE lock_key = ? AND lock_value = ?;";
  public static final String DELETE_EXPIRED_FORMATTED_QUERY = "DELETE FROM %s WHERE expire_at < ?;";
  public static final String REFRESH_FORMATTED_QUERY = "UPDATE %s SET expire_at = ? WHERE lock_key = ? AND lock_value = ?;";
  private static final Logger LOGGER = LoggerFactory
      .getLogger(SimpleJdbcDistributedLockProvider.class);
  private final JdbcTemplate jdbcTemplate;

  public SimpleJdbcDistributedLockProvider(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
  public boolean acquire(DistributedLock distributedLock) {
    // 每次获取锁之前，先将过期的锁删除
    // 因为时钟漂移问题，可能会将不应该释放的锁释放掉了，后期在想办法解决这个问题
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
    return result;
  }

  @Override
  @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
  public void hold(DistributedLock distributedLock) {
    long now = System.currentTimeMillis();
    long expireAt = System.currentTimeMillis() + distributedLock.expireMills();

    final int updated = jdbcTemplate
        .update(String.format(REFRESH_FORMATTED_QUERY, distributedLock.storeName()), expireAt,
            distributedLock.lockKey(), distributedLock.lockValue());
    final boolean refreshed = updated == 1;
    if (refreshed) {
      LOGGER.debug("Refresh successfully, affected 1 record for key {} with value {} in store {}",
          distributedLock.lockKey(), distributedLock.lockValue(), distributedLock.storeName());
    } else if (updated > 0) {
      LOGGER
          .error("Unexpected result from refresh for key {} with value {} in store {}, released {}",
              distributedLock.lockKey(), distributedLock.lockValue(), distributedLock.storeName(),
              updated);
    } else {
      LOGGER.error("Refresh did not affect any records for key {} with value {} in store {}",
          distributedLock.lockKey(), distributedLock.lockValue(), distributedLock.storeName());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
  public boolean release(String storeName, String lockKey, String lockValue) {
    // 必须是同一个客户端才能释放锁
    final int deleted = jdbcTemplate
        .update(String.format(RELEASE_FORMATTED_QUERY, storeName), lockKey, lockValue);

    final boolean released = deleted == 1;
    if (released) {
      LOGGER.debug("Release successfully, affected 1 record for key {} with value {} in store {}",
          lockKey, lockValue, storeName);
    } else if (deleted > 0) {
      LOGGER
          .error("Unexpected result from release for key {} with value {} in store {}, released {}",
              lockKey, lockValue, storeName, deleted);
    } else {
      LOGGER.error("Release did not affect any records for key {} with value {} in store {}",
          lockKey, lockValue, storeName);
    }

    return released;
  }

  private boolean doAcquire(DistributedLock distributedLock) {
    long now = System.currentTimeMillis();
    final int expired = jdbcTemplate
        .update(String.format(DELETE_EXPIRED_FORMATTED_QUERY, distributedLock.storeName()), now);
    LOGGER.debug("Expired {} locks", expired);

    try {
      now = System.currentTimeMillis();
      final long expireAt = now + distributedLock.expireMills();
      final int created = jdbcTemplate
          .update(String.format(ACQUIRE_FORMATTED_QUERY, distributedLock.storeName()),
              distributedLock.lockKey(), distributedLock.lockValue(), now, expireAt);
      return created == 1;
    } catch (final DuplicateKeyException e) {
      return false;
    }
  }
}
