package com.github.edgar615.spring.jdbc;

import com.github.edgar615.util.db.Jdbc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"application.yml"})
public class JdbcTest {

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
