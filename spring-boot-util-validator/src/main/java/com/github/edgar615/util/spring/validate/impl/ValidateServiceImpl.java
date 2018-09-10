package com.github.edgar615.util.spring.validate.impl;

import com.github.edgar615.util.reflect.BeanUtils;
import com.github.edgar615.util.spring.validate.ValidateService;
import com.github.edgar615.util.validation.Rule;
import com.github.edgar615.util.validation.Validations;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 校验类的实现.
 *
 * @author Edgar
 * @create 2018-09-10 15:28
 **/
public class ValidateServiceImpl implements ValidateService {

  private final Map<String, Multimap<String, Rule>> registry = new HashMap<>();

  @Override
  public void validate(String key, Map<String, Object> params) {
    validate(key, ArrayListMultimap.create(), params);
  }

  @Override
  public void validate(String key, Multimap<String, Rule> rules, Map<String, Object> params) {
    Multimap<String, Rule> regRule = ArrayListMultimap
        .create(registry.getOrDefault(key, ArrayListMultimap.create()));
    regRule.putAll(rules);
    Validations.validate(params, regRule);
  }

  @Override
  public void validate(String key, Object target) {
    validate(key, ArrayListMultimap.create(), target);
  }

  @Override
  public void validate(String key, Multimap<String, Rule> rules, Object target) {
    Map<String, Object> map = BeanUtils.toMap(target);
    validate(key, rules, map);
  }

  public void addRule(String key, Multimap<String, Rule> rules) {
    Objects.requireNonNull(key);
    Objects.requireNonNull(rules);
    registry.put(key, rules);
  }
}
