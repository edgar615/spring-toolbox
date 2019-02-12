package com.github.edgar615.util.spring.jdbc;

import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.search.Example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;

/**
 * Created by Edgar on 2017/8/8.
 *
 * @author Edgar  Date 2017/8/8
 */
@CacheConfig(cacheResolver = "jdbcCacheResolver")
public class CachedJdbcOperationImpl implements JdbcOperation {

  private final JdbcOperation jdbcOperation;

  private final FindByIdAction findByIdAction;

  public CachedJdbcOperationImpl(DataSource dataSource, FindByIdAction findByIdAction) {
    this.jdbcOperation = new JdbcOperationImpl(dataSource);
    this.findByIdAction = findByIdAction;
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", key = "#p0.getClass().getSimpleName() + ':' + #p0.id()")
  public <ID> void insert(Persistent<ID> persistent) {
    jdbcOperation.insert(persistent);
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", key = "#p0.getClass().getSimpleName() + ':' + #result")
  public <ID> ID insertAndGeneratedKey(Persistent<ID> persistent) {
    return jdbcOperation.insertAndGeneratedKey(persistent);
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", allEntries = true)
  public <ID, T extends Persistent<ID>> void batchInsert(List<T> persistentList) {
    jdbcOperation.batchInsert(persistentList);
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", key = "#p0.getSimpleName() + ':' + #p1")
  public <ID, T extends Persistent<ID>> int deleteById(Class<T> elementType, ID id) {
    return jdbcOperation.deleteById(elementType, id);
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", allEntries = true)
  public <ID, T extends Persistent<ID>> int deleteByExample(Class<T> elementType,
      Example example) {
    return jdbcOperation.deleteByExample(elementType, example);
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", key = "#p0.getClass().getSimpleName() + ':' + #p3")
  public <ID> int updateById(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields, ID id) {
    return jdbcOperation.updateById(persistent, addOrSub, nullFields, id);
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", allEntries = true)
  public <ID> int updateByExample(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields,
      Example example) {
    return jdbcOperation.updateByExample(persistent, addOrSub, nullFields, example);
  }

  @Override
  public <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id,
      List<String> fields) {
    T result = findByIdAction.findById(elementType, id);
    if (fields == null || fields.isEmpty()) {
      return result;
    }
    Map<String, Object> map = result.toMap();
    Map<String, Object> newMap = new HashMap<>();
    fields.stream().forEach(f -> newMap.put(f, map.get(f)));
    Persistent<ID> newResult = Persistent.create(elementType);
    newResult.fromMap(newMap);
    return (T) newResult;
  }

  /**
   * 根据主键更新，忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @return 修改记录数
   */
  @Override
  @CacheEvict(cacheNames = "JdbcCache", key = "#p0.getClass().getSimpleName() + ':' + #p1")
  public <ID> int updateById(Persistent<ID> persistent, ID id) {
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
  @Override
  @CacheEvict(cacheNames = "JdbcCache", allEntries = true)
  public <ID> int updateByExample(Persistent<ID> persistent, Example example) {
    return updateByExample(persistent, new HashMap<>(), new ArrayList<>(), example);
  }

  @Override
  public <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id) {
    return findByIdAction.findById(elementType, id);
  }

  @Override
  public <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType,
      Example example) {
    return jdbcOperation.findByExample(elementType, example);
  }

  @Override
  public <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType, Example example,
      int start, int limit) {
    return jdbcOperation.findByExample(elementType, example, start, limit);
  }

  @Override
  public <ID, T extends Persistent<ID>> int countByExample(Class<T> elementType,
      Example example) {
    return jdbcOperation.countByExample(elementType, example);
  }

}
