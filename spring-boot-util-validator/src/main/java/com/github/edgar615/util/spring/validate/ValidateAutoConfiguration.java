package com.github.edgar615.util.spring.validate;

import com.github.edgar615.util.spring.validate.impl.ValidateServiceImpl;
import com.github.edgar615.util.validation.Rule;
import com.github.edgar615.util.validation.RuleManager;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ValidateProperty.class})
public class ValidateAutoConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "validate")
  @ConditionalOnMissingBean(ValidateService.class)
  public ValidateService validateService(ValidateProperty validateProperty) {
    System.out.println(validateProperty);
    ValidateServiceImpl validateService = new ValidateServiceImpl();
    RuleManager ruleManager = RuleManager.instance();
    for (Map.Entry<String, Map<String, String>> entry : validateProperty.getSpec().entrySet()) {
      String key = entry.getKey();
      Map<String, String> spec = entry.getValue();
      Multimap<String, Rule> rules = ArrayListMultimap.create();
      for (Map.Entry<String, String> field : spec.entrySet()) {
        rules.putAll(field.getKey(), ruleManager.parse(field.getValue()));
      }
      validateService.addRule(key, rules);
    }
    return validateService;
  }

}
