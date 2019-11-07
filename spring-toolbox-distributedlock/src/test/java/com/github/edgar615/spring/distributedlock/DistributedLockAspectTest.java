package com.github.edgar615.spring.distributedlock;

import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.ErrorCode;
import com.github.edgar615.util.exception.SystemException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DistributedLockAspectTest {

  @Autowired
  private OrderService orderService;

  @Autowired
  private DistributedLockProvider distributedLockProvider;

  @After
  public void tearDown() {
    ((MockLocalLockProvier) distributedLockProvider).clear();
  }

  @Test
  public void testLock() {
    // 锁
    String orderNo = UUID.randomUUID().toString().replace("-", "");
    Thread thread = new Thread(() -> {
      orderService.pay(orderNo, 3);
    });
    thread.start();
    try {
      TimeUnit.MILLISECONDS.sleep(1500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    int count = ((MockLocalLockProvier) distributedLockProvider).count();
    Assert.assertEquals(1, count);
    try {
      orderService.pay(orderNo, 0);
    } catch (Exception e) {
      Assert.assertTrue(e instanceof SystemException);
      ErrorCode errorCode = ((SystemException) e).getErrorCode();
      Assert.assertEquals(DefaultErrorCode.RESOURCE_OCCUPIED, errorCode);
      return;
    }
    Assert.fail();
  }

  @Test
  public void testRelease() {
    // 锁
    String orderNo = UUID.randomUUID().toString().replace("-", "");
    Thread thread = new Thread(() -> {
      orderService.pay(orderNo, 1);
    });
    thread.start();
    try {
      TimeUnit.MILLISECONDS.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    int count = ((MockLocalLockProvier) distributedLockProvider).count();
    Assert.assertEquals(0, count);
    orderService.pay(orderNo, 0);
  }

}
