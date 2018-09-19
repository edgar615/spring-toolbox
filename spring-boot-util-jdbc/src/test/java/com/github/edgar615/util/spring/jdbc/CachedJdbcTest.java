package com.github.edgar615.util.spring.jdbc;

import com.github.edgar615.util.db.Jdbc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CachedJdbcTest {

  @Autowired
  private Jdbc jdbc;

  @Test
  public void testFindFirst() {
    Device device = jdbc.findById(Device.class, 1);
    System.out.println(device);

    device = jdbc.findById(Device.class, 1);
    System.out.println(device);

    device = new Device();
    device.setBarcode("1");
    jdbc.updateById(device, 1);

    jdbc.findById(Device.class, 1);
  }
}