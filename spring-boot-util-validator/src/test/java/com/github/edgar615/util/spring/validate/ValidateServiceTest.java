package com.github.edgar615.util.spring.validate;

import com.github.edgar615.util.validation.Rule;
import com.github.edgar615.util.validation.ValidationException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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
    Multimap<String, Rule> rules = ArrayListMultimap.create();
    rules.put("macAddress", Rule.required());
    try {
      validateService.validate("insert_device",rules, map);
    } catch (ValidationException e) {
      return;
    }
    Assert.fail();
  }

  @Test
  public void testValidateMap3() {
    Map<String, Object> map = new HashMap<>();
    map.put("barcode", "LH20401111111111111");
    try {
      validateService.validate("insert_device", map);
    } catch (ValidationException e) {
      Assert.fail();
    }
  }

  @Test
  public void testValidateBean() {
    Device device = new Device();
    try {
      validateService.validate("insert_device", device);
    } catch (ValidationException e) {
      return;
    }
    Assert.fail();
  }

  @Test
  public void testValidateBean2() {
    Device device = new Device();
    device.setBarcode("LH20401111111111111");
    Multimap<String, Rule> rules = ArrayListMultimap.create();
    rules.put("macAddress", Rule.required());
    try {
      validateService.validate("insert_device",rules, device);
    } catch (ValidationException e) {
      return;
    }
    Assert.fail();
  }

  @Test
  public void testValidateBean3() {
    Device device = new Device();
    device.setBarcode("LH20401111111111111");
    try {
      validateService.validate("insert_device", device);
    } catch (ValidationException e) {
      Assert.fail();
    }
  }
}