package com.github.edgar615.spring.distributedlock.redis;

import com.github.edgar615.spring.distributedlock.DistributedLock;
import com.github.edgar615.spring.distributedlock.DistributedLockImpl;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SimpleRedisDistributedLockProviderTest {

  @Autowired
  private SimpleRedistDistributedLockProvider simpleRedistDistributedLockProvider;

  @Test
  public void testLock() {
    // 锁
    String orderNo = UUID.randomUUID().toString().replace("-", "");
    String client = UUID.randomUUID().toString().replace("-", "");
    DistributedLock distributedLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, client, 1000L);
    boolean locked1 = simpleRedistDistributedLockProvider.acquire(distributedLock);
    Assert.assertTrue(locked1);

    boolean locked2 = simpleRedistDistributedLockProvider.acquire(distributedLock);
    Assert.assertFalse(locked2);

    String anotherClient = UUID.randomUUID().toString().replace("-", "");
    DistributedLock anotherClientLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, anotherClient, 1000L);
    boolean locked3 = simpleRedistDistributedLockProvider.acquire(anotherClientLock);
    Assert.assertFalse(locked3);

    String anotherOrderNo = UUID.randomUUID().toString().replace("-", "");
    DistributedLock anotherDistributedLock = new DistributedLockImpl("distributed_lock", "order:" + anotherOrderNo, client, 1000L);
    boolean locked4 = simpleRedistDistributedLockProvider.acquire(anotherDistributedLock);
    Assert.assertTrue(locked4);

    // 不同的客户端无法释放锁
    boolean release1 = simpleRedistDistributedLockProvider.release("distributed_lock", "order:" + orderNo, anotherClient);
    Assert.assertFalse(release1);

    // 释放锁
    boolean release2 = simpleRedistDistributedLockProvider.release("distributed_lock", "order:" + orderNo, client);
    Assert.assertTrue(release2);

    // 释放锁
    boolean release3 = simpleRedistDistributedLockProvider.release("distributed_lock", "order:" + anotherOrderNo, client);
    Assert.assertTrue(release3);
  }

  @Test
  public void testReleaseExpiredLock() throws InterruptedException {
    // 锁
    String orderNo = UUID.randomUUID().toString().replace("-", "");
    String client = UUID.randomUUID().toString().replace("-", "");
    DistributedLock distributedLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, client, 500L);
    boolean locked1 = simpleRedistDistributedLockProvider.acquire(distributedLock);
    Assert.assertTrue(locked1);

    TimeUnit.SECONDS.sleep(1);
    String anotherClient = UUID.randomUUID().toString().replace("-", "");
    DistributedLock anotherClientLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, anotherClient, 1000L);
    boolean locked2 = simpleRedistDistributedLockProvider.acquire(anotherClientLock);
    Assert.assertTrue(locked2);

    // 不同的客户端无法释放锁
    boolean release1 = simpleRedistDistributedLockProvider.release("distributed_lock", "order:" + orderNo, anotherClient);
    Assert.assertTrue(release1);
  }

  @Test
  public void testHoldSuccess() throws InterruptedException {
    // 锁
    String orderNo = UUID.randomUUID().toString().replace("-", "");
    String client = UUID.randomUUID().toString().replace("-", "");
    DistributedLock distributedLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, client, 1000L);
    boolean locked1 = simpleRedistDistributedLockProvider.acquire(distributedLock);
    Assert.assertTrue(locked1);
    String anotherClient = UUID.randomUUID().toString().replace("-", "");
    DistributedLock anotherExpiredLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, client, 5000L);
    simpleRedistDistributedLockProvider.hold(anotherExpiredLock);

    TimeUnit.SECONDS.sleep(2);
    // hold成功，无法获得锁
    DistributedLock anotherClientLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, anotherClient, 1000L);
    boolean locked2 = simpleRedistDistributedLockProvider.acquire(anotherClientLock);
    Assert.assertFalse(locked2);

    // 释放锁
    boolean release1 = simpleRedistDistributedLockProvider.release("distributed_lock", "order:" + orderNo, client);
    Assert.assertTrue(release1);
  }

  @Test
  public void testHoldFaildByOtherClient() throws InterruptedException {
    // 锁
    String orderNo = UUID.randomUUID().toString().replace("-", "");
    String client = UUID.randomUUID().toString().replace("-", "");
    DistributedLock distributedLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, client, 1000L);
    boolean locked1 = simpleRedistDistributedLockProvider.acquire(distributedLock);
    Assert.assertTrue(locked1);

    String anotherClient = UUID.randomUUID().toString().replace("-", "");
    DistributedLock anotherClientLock = new DistributedLockImpl("distributed_lock", "order:" + orderNo, anotherClient, 1000L);
    simpleRedistDistributedLockProvider.hold(anotherClientLock);
    TimeUnit.SECONDS.sleep(1);
    boolean locked2 = simpleRedistDistributedLockProvider.acquire(anotherClientLock);
    Assert.assertTrue(locked2);

    // 释放锁
    boolean release1 = simpleRedistDistributedLockProvider.release("distributed_lock", "order:" + orderNo, anotherClient);
    Assert.assertTrue(release1);
  }
}
