package com.github.edgar615.spring.jdbc;

import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.search.Example;
import com.github.edgar615.util.search.MoreExample;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.cache.annotation.CacheEvict;

/**
 * JDBC的操作
 */
public interface JdbcOperation {

  /**
   * 新增数据.
   *
   * @param persistent 持久化对象
   * @param <ID> 主键类型
   */
  <ID> void insert(Persistent<ID> persistent);

  /**
   * insert一条数据，并返回自增主键
   *
   * @param persistent 持久化对象
   * @param <ID> 主键类型
   */
  <ID> ID insertAndGeneratedKey(Persistent<ID> persistent);

  /**
   * 批量插入
   *
   * @param persistentList 持久化对象的集合
   * @param <ID> 主键列席
   * @param <T> 持久化对象
   */
  <ID, T extends Persistent<ID>> void batchInsert(List<T> persistentList);

  /**
   * 根据主键删除.
   *
   * @param elementType 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 删除记录数
   */
  <ID, T extends Persistent<ID>> int deleteById(Class<T> elementType, ID id);

  /**
   * 根据条件删除.
   *
   * @param elementType 持久化对象
   * @param example 查询条件
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 删除记录数
   */
  <ID, T extends Persistent<ID>> int deleteByExample(Class<T> elementType, Example example);

  /**
   * 根据主键更新，忽略实体中的null
   *
   * @param persistent 持久化对象
   * @param addOrSub 需要做增加或者减去的字段，value为正数表示增加，负数表示减少
   * @param nullFields 需要设为null的字段
   * @param id 主键ID
   * @param <ID> 主键类型
   * @return 修改记录数
   */
  <ID> int updateById(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields,
      ID id);

  /**
   * 根据条件更新，忽略实体中的null
   *
   * @param persistent 持久化对象
   * @param addOrSub 需要做增加或者减去的字段，value为正数表示增加，负数表示减少
   * @param nullFields 需要设为null的字段
   * @param example 查询条件
   * @param <ID> 主键类型
   * @return 修改记录数
   */
  <ID> int updateByExample(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields, Example example);

  /**
   * 根据主键查找.
   *
   * @param elementType 持久化对象
   * @param id 主键
   * @param fields 返回的属性列表
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 持久化对象
   */
  <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id, List<String> fields);

  /**
   * 根据条件查找.
   *
   * @param elementType 持久化对象,
   * @param example 查询参数的定义，包括查询条件、排序规则等
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 持久化对象列表
   */
  <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType, Example example);

  /**
   * 根据条件查找.
   *
   * @param elementType 持久化对象
   * @param example 查询参数的定义，包括查询条件、排序规则等
   * @param start 开始索引
   * @param limit 查询数量
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 持久化对象列表
   */
  <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType, Example example,
      int start,
      int limit);

  /**
   * 根据条件查找.
   *
   * @param elementType 持久化对象
   * @param example 查询条件
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 记录数
   */
  <ID, T extends Persistent<ID>> int countByExample(Class<T> elementType, Example example);

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

  <ID, T extends Persistent<ID>> int deleteByMoreExample(Class<T> elementType,
      MoreExample example);

  <ID> int updateByMoreExample(Persistent<ID> persistent, MoreExample example);

  <ID> int updateByMoreExample(Persistent<ID> persistent, Map<String, Number> addOrSub,
      List<String> nullFields, MoreExample example);

  <ID, T extends Persistent<ID>> List<T> findByMoreExample(Class<T> elementType,
      MoreExample example);

  <ID, T extends Persistent<ID>> List<T> findByMoreExample(Class<T> elementType,
      MoreExample example, int start, int limit);

  <ID, T extends Persistent<ID>> int countByMoreExample(Class<T> elementType,
      MoreExample example);

  /**
   * 根据主键更新，忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @return 修改记录数
   */
  default <ID> int updateById(Persistent<ID> persistent, ID id) {
    return updateById(persistent, new HashMap<>(), new ArrayList<>(), id);
  }

  /**
   * 根据条件更新，忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param example 查询条件
   * @param <ID> 条件集合
   * @return 修改记录数
   */
  default <ID> int updateByExample(Persistent<ID> persistent, Example example) {
    return updateByExample(persistent, new HashMap<>(), new ArrayList<>(), example);
  }

}
