package com.github.edgar615.spring.jdbc;

import com.github.edgar615.util.db.Persistent;
import com.google.common.collect.Lists;

/**
 * 因为jdbc中存在两个findById的方法，如果在jdbc方法上加上缓存，两个方法总会有一个无法获得正确的结果，所以将这个功能单独作为一个类来实现
 */
public interface FindByIdAction {

  /**
   * 根据主键查找.
   *
   * @param elementType 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 持久化对象，如果未找到任何数据，返回null
   */
  <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id);
}
