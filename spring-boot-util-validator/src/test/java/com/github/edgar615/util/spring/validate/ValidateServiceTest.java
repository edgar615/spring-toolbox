package com.github.edgar615.util.spring.validate;

import com.github.edgar615.util.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidateServiceTest {

  @Autowired
  private ValidateService validateService;

  @Test
  public void testValidateMap() {
    Map<String, Object> map = new HashMap<>();
    try {
      validateService.validate("insert_device", map);
    } catch (ValidationException e) {
      return;
    }
    Assert.fail();
  }

  @Test
  public void testValidateMap2() {
    Map<String, Object> map = new HashMap<>();
    map.put("barcode", "LH20401111111111111");
    try {
      validateService.validate("insert_device", map);
    } catch (ValidationException e) {
      Assert.fail();
    }
  }
}