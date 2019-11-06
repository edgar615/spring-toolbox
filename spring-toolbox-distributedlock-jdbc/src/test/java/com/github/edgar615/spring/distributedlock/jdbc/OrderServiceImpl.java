package com.github.edgar615.spring.distributedlock.jdbc;

import com.github.edgar615.spring.distributedlock.DistributeLock;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  @Override
  @DistributeLock(lockKey = "'order:' + #p0", expireMills = 1000L, lockValue = "#applicationId")
  public boolean pay(String orderNo, int sleepSecond) {
    if (sleepSecond > 0) {
      try {
        TimeUnit.SECONDS.sleep(sleepSecond);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return true;
  }
}
