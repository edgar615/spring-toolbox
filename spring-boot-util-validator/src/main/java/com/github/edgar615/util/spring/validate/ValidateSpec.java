package com.github.edgar615.util.spring.validate;

import java.util.Map;

/**
 * 校验规则定义.
 *
 * @author Edgar
 * @create 2018-09-10 17:49
 **/
public class ValidateSpec {

  private Map<String, String> fields;

  public Map<String, String> getFields() {
    return fields;
  }

  public void setFields(Map<String, String> fields) {
    this.fields = fields;
  }
}
