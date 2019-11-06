package com.github.edgar615.spring.validate;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 校验类的配置.
 *
 * @author Edgar
 * @create 2018-09-10 16:46
 **/
@ConfigurationProperties(prefix = "validate")
public class ValidateProperty {

  private Map<String, Map<String, String>> spec;

  public Map<String, Map<String, String>> getSpec() {
    return spec;
  }

  public void setSpec(
      Map<String, Map<String, String>> spec) {
    this.spec = spec;
  }
}
