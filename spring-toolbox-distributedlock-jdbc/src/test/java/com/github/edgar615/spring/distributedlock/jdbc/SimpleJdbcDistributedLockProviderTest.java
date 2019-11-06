package com.github.edgar615.spring.distributedlock.jdbc;

import com.github.edgar615.spring.distributedlock.DistributedLock;
import com.github.edgar615.spring.distributedlock.DistributedLockImpl;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SimpleJdbcDistributedLockProviderTest {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private SimpleJdbcDistributedLockProvider simpleJdbcDistributedLockProvider;

  @Test
  public void testLock() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    // 锁
    String orderNo = UUID.randomUUID().toString().replace("-", "");
    String client = UUID.randomUUID().toString().replace("-", "");
    DistributedLock distributedLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, client, 1000L);
    boolean locked1 = simpleJdbcDistributedLockProvider.acquire(distributedLock);
    Assert.assertTrue(locked1);

    boolean locked2 = simpleJdbcDistributedLockProvider.acquire(distributedLock);
    Assert.assertFalse(locked2);

    String anotherClient = UUID.randomUUID().toString().replace("-", "");
    DistributedLock anotherClientLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, anotherClient, 1000L);
    boolean locked3 = simpleJdbcDistributedLockProvider.acquire(anotherClientLock);
    Assert.assertFalse(locked3);

    String anotherOrderNo = UUID.randomUUID().toString().replace("-", "");
    DistributedLock anotherDistributedLock = new DistributedLockImpl("distributed_lock", "order:" + anotherOrderNo, client, 1000L);
    boolean locked4 = simpleJdbcDistributedLockProvider.acquire(anotherDistributedLock);
    Assert.assertTrue(locked4);

    // 检查数据库中有2条数据
    List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from distributed_lock");
    Assert.assertEquals(result.size(), 2);

    // 不同的客户端无法释放锁
    boolean release1 = simpleJdbcDistributedLockProvider.release("distributed_lock", "order:" + orderNo, anotherClient);
    Assert.assertFalse(release1);
    result = jdbcTemplate.queryForList("select * from distributed_lock");
    Assert.assertEquals(result.size(), 2);

    // 释放锁
    boolean release2 = simpleJdbcDistributedLockProvider.release("distributed_lock", "order:" + orderNo, client);
    Assert.assertTrue(release2);
    result = jdbcTemplate.queryForList("select * from distributed_lock");
    Assert.assertEquals(result.size(), 1);

    // 释放锁
    boolean release3 = simpleJdbcDistributedLockProvider.release("distributed_lock", "order:" + anotherOrderNo, client);
    Assert.assertTrue(release3);
    result = jdbcTemplate.queryForList("select * from distributed_lock");
    Assert.assertEquals(result.size(), 0);
  }

  @Test
  public void testReleaseExpiredLock() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    // 锁
    String orderNo = UUID.randomUUID().toString().replace("-", "");
    String client = UUID.randomUUID().toString().replace("-", "");
    // 人为做了一个错误的数据
    DistributedLock distributedLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, client, -10000L);
    boolean locked1 = simpleJdbcDistributedLockProvider.acquire(distributedLock);
    Assert.assertTrue(locked1);

    String anotherClient = UUID.randomUUID().toString().replace("-", "");
    DistributedLock anotherClientLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, anotherClient, 1000L);
    boolean locked2 = simpleJdbcDistributedLockProvider.acquire(anotherClientLock);
    Assert.assertTrue(locked2);

    // 检查数据库中有2条数据
    List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from distributed_lock");
    Assert.assertEquals(result.size(), 1);

    // 不同的客户端无法释放锁
    boolean release1 = simpleJdbcDistributedLockProvider.release("distributed_lock", "order:" + orderNo, anotherClient);
    Assert.assertTrue(release1);
    result = jdbcTemplate.queryForList("select * from distributed_lock");
    Assert.assertEquals(result.size(), 0);
  }

  @Test
  public void testHoldSuccess() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    // 锁
    String orderNo = UUID.randomUUID().toString().replace("-", "");
    String client = UUID.randomUUID().toString().replace("-", "");
    // 人为做了一个错误的数据
    DistributedLock distributedLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, client, -10000L);
    boolean locked1 = simpleJdbcDistributedLockProvider.acquire(distributedLock);
    Assert.assertTrue(locked1);

    // 检查数据库中有2条数据
    List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from distributed_lock");
    Assert.assertEquals(result.size(), 1);

    String anotherClient = UUID.randomUUID().toString().replace("-", "");
    DistributedLock anotherExpiredLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, client, 1000L);
    simpleJdbcDistributedLockProvider.hold(anotherExpiredLock);
    // hold成功，无法获得锁
    DistributedLock anotherClientLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, anotherClient, 1000L);
    boolean locked2 = simpleJdbcDistributedLockProvider.acquire(anotherClientLock);
    Assert.assertFalse(locked2);

    // 释放锁
    boolean release1 = simpleJdbcDistributedLockProvider.release("distributed_lock", "order:" + orderNo, client);
    Assert.assertTrue(release1);
    result = jdbcTemplate.queryForList("select * from distributed_lock");
    Assert.assertEquals(result.size(), 0);
  }

  @Test
  public void testHoldFaildByOtherClient() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    // 锁
    String orderNo = UUID.randomUUID().toString().replace("-", "");
    String client = UUID.randomUUID().toString().replace("-", "");
    // 人为做了一个错误的数据
    DistributedLock distributedLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, client, -10000L);
    boolean locked1 = simpleJdbcDistributedLockProvider.acquire(distributedLock);
    Assert.assertTrue(locked1);

    // 检查数据库中有2条数据
    List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from distributed_lock");
    Assert.assertEquals(result.size(), 1);

    String anotherClient = UUID.randomUUID().toString().replace("-", "");
    DistributedLock anotherClientLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, anotherClient, 1000L);
    simpleJdbcDistributedLockProvider.hold(anotherClientLock);
    // hold失败，可以正常获得锁
    boolean locked2 = simpleJdbcDistributedLockProvider.acquire(anotherClientLock);
    Assert.assertTrue(locked2);

    // 释放锁
    boolean release1 = simpleJdbcDistributedLockProvider.release("distributed_lock", "order:" + orderNo, anotherClient);
    Assert.assertTrue(release1);
    result = jdbcTemplate.queryForList("select * from distributed_lock");
    Assert.assertEquals(result.size(), 0);
  }
}
