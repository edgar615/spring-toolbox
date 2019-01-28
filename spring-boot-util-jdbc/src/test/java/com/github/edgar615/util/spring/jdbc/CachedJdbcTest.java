package com.github.edgar615.util.spring.jdbc;

import com.github.edgar615.util.db.Jdbc;
import com.github.edgar615.util.search.Example;
import com.google.common.collect.Lists;
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
    Device device = jdbc.findById(Device.class, 3);
    System.out.println(device);

    device = jdbc.findById(Device.class, 3);
    System.out.println(device);

    device = new Device();
    device.setBarcode("3");
    jdbc.updateById(device, 3);

    jdbc.findById(Device.class, 3);
  }

  @Test
  public void testField() {
    Device device = jdbc.findById(Device.class, 3);
    System.out.println(device);

    device = jdbc.findById(Device.class, 3, Lists.newArrayList("deviceId"));
    System.out.println(device);

    device = new Device();
    device.setBarcode("3");
    Example example = Example.create()
        .equalsTo("deviceId", 1);
    jdbc.updateByExample(device, example);

    jdbc.findById(Device.class, 3);
  }
}
