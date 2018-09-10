package com.github.edgar615.util.spring.validate;

import com.github.edgar615.util.validation.Rule;
import com.google.common.collect.Multimap;
import java.util.Map;

/**
 * 校验Service.
 *
 * @author Edgar
 * @create 2018-09-10 15:24
 **/
public interface ValidateService {

  /**
   * 校验一个map对象
   *
   * @param key 校验规则在注册表里的key
   * @param params 待校验的MAP对象
   */
  void validate(String key, final Map<String, Object> params);


  /**
   * 校验一个map对象.
   *
   * 除注册表内的规则外，可以添加额外的规则
   *
   * @param key 校验规则在注册表里的key
   * @param rules 除注册表内的规则外，可以添加额外的规则
   * @param params 待校验的MAP对象
   */
  void validate(String key, Multimap<String, Rule> rules, final Map<String, Object> params);

  /**
   * 校验一个map对象
   *
   * @param key 校验规则在注册表里的key
   * @param target 待校验的对象
   */
  void validate(String key, final Object target);

  /**
   * 校验一个map对象
   *
   * 除注册表内的规则外，可以添加额外的规则
   *
   * @param key 校验规则在注册表里的key
   * @param target 待校验的对象
   */
  void validate(String key, Multimap<String, Rule> rules, final Object target);
}
