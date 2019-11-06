package com.github.edgar615.spring.jdbc;

import com.github.edgar615.util.db.Jdbc;
import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.search.Example;
import com.github.edgar615.util.search.MoreExample;
import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2017/8/8.
 *
 * @author Edgar  Date 2017/8/8
 */
public class JdbcImpl implements Jdbc {

  private final JdbcOperation jdbcOperation;

  public JdbcImpl(JdbcOperation jdbcOperation) {
    this.jdbcOperation = jdbcOperation;
  }

  @Override
  public <ID> void insert(Persistent<ID> persistent) {
    jdbcOperation.insert(persistent);
  }

  @Override
  public <ID> ID insertAndGeneratedKey(Persistent<ID> persistent) {
    return jdbcOperation.insertAndGeneratedKey(persistent);
  }

  @Override
  public <ID, T extends Persistent<ID>> void batchInsert(List<T> persistentList) {
    jdbcOperation.batchInsert(persistentList);
  }

  @Override
  public <ID, T extends Persistent<ID>> int deleteById(Class<T> elementType, ID id) {
    return jdbcOperation.deleteById(elementType, id);
  }

  @Override
  public <ID, T extends Persistent<ID>> int deleteByExample(Class<T> elementType,
      Example example) {
    return jdbcOperation.deleteByExample(elementType, example);
  }

  @Override
  public <ID> int updateById(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields, ID id) {
    return jdbcOperation.updateById(persistent, addOrSub, nullFields, id);
  }

  @Override
  public <ID> int updateByExample(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields,
      Example example) {
    return jdbcOperation.updateByExample(persistent, addOrSub, nullFields, example);
  }

  @Override
  public <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id,
      List<String> fields) {
    return jdbcOperation.findById(elementType, id, fields);
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

  @Override
  public <ID, T extends Persistent<ID>> int deleteByMoreExample(Class<T> elementType,
      MoreExample example) {
    return jdbcOperation.deleteByMoreExample(elementType, example);
  }

  @Override
  public <ID> int updateByMoreExample(Persistent<ID> persistent, Map<String, Number> addOrSub,
      List<String> nullFields, MoreExample example) {
    return jdbcOperation.updateByMoreExample(persistent, addOrSub, nullFields, example);
  }

  @Override
  public <ID, T extends Persistent<ID>> List<T> findByMoreExample(Class<T> elementType,
      MoreExample example) {
    return jdbcOperation.findByMoreExample(elementType, example);
  }

  @Override
  public <ID, T extends Persistent<ID>> List<T> findByMoreExample(Class<T> elementType,
      MoreExample example, int start, int limit) {
    return jdbcOperation.findByMoreExample(elementType, example, start, limit);
  }

  @Override
  public <ID, T extends Persistent<ID>> int countByMoreExample(Class<T> elementType,
      MoreExample example) {
    return jdbcOperation.countByMoreExample(elementType, example);
  }

}
