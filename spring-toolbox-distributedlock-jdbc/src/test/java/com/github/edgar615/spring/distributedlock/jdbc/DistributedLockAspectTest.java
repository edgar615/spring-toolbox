package com.github.edgar615.spring.distributedlock.jdbc;

import com.github.edgar615.spring.distributedlock.DistributedLock;
import com.github.edgar615.spring.distributedlock.DistributedLockImpl;
import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.ErrorCode;
import com.github.edgar615.util.exception.SystemException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
public class DistributedLockAspectTest {

  @Autowired
  private OrderService orderService;

  @Autowired
  private LockService lockService;

  @Test
  public void testLock() {
    // 锁
    String orderNo = UUID.randomUUID().toString().replace("-", "");
    Thread thread = new Thread(() ->  {
      orderService.pay(orderNo, 3);
    });
    thread.start();
    try {
      TimeUnit.MILLISECONDS.sleep(1500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    int count = lockService.count();
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

}
