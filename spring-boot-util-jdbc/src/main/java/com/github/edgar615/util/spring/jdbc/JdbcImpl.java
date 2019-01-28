package com.github.edgar615.util.spring.jdbc;

import com.github.edgar615.util.base.StringUtils;
import com.github.edgar615.util.db.Jdbc;
import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.db.SQLBindings;
import com.github.edgar615.util.db.SqlBuilder;
import com.github.edgar615.util.search.Example;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Created by Edgar on 2017/8/8.
 *
 * @author Edgar  Date 2017/8/8
 */
@CacheConfig(cacheResolver = "jdbcCacheResolver")
public class JdbcImpl implements Jdbc {

  private final DataSource dataSource;

  private final FindByIdAction findByIdAction;

  public JdbcImpl(DataSource dataSource, FindByIdAction findByIdAction) {
    this.dataSource = dataSource;
    this.findByIdAction = findByIdAction;
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", key = "#p0.id")
  @JdbcCache("#p0.getClass().getSimpleName()")
  public <ID> void insert(Persistent<ID> persistent) {
    SQLBindings sqlBindings = SqlBuilder.insert(persistent);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", key = "#result")
  @JdbcCache("#p0.getClass().getSimpleName()")
  public <ID> ID insertAndGeneratedKey(Persistent<ID> persistent) {
    SQLBindings sqlBindings = SqlBuilder.insert(persistent);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps
              = connection.prepareStatement(sqlBindings.sql(),
              new String[]{StringUtils.underscoreName(
                  persistent.primaryField())}
          );
          int i = 1;
          for (Object arg : sqlBindings.bindings()) {
            ps.setObject(i++, arg);
          }
          return ps;
        },
        keyHolder);
    persistent.setGeneratedKey(keyHolder.getKey());
    return persistent.id();
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", allEntries = true)
  @JdbcCache("#p0.get(0).getClass().getSimpleName()")
  public <ID, T extends Persistent<ID>> void batchInsert(List<T> persistentList) {
    if (persistentList == null || persistentList.isEmpty()) {
      return;
    }
    if (persistentList.size() == 1) {
      insert(persistentList.get(0));
      return;
    }
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    List<SQLBindings> sqlBindingsList = persistentList.stream()
        .map(p -> SqlBuilder.fullInsertSql(p))
        .collect(Collectors.toList());
    String sql = sqlBindingsList.get(0).sql();
    List<Object[]> args = sqlBindingsList.stream()
        .map(sqlBindings -> sqlBindings.bindings().toArray())
        .collect(Collectors.toList());
    jdbcTemplate.batchUpdate(sql, args);
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", key = "#p1")
  @JdbcCache("#p0.getSimpleName()")
  public <ID, T extends Persistent<ID>> int deleteById(Class<T> elementType, ID id) {
    SQLBindings sqlBindings = SqlBuilder.deleteById(elementType, id);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", allEntries = true)
  @JdbcCache("#p0.getSimpleName()")
  public <ID, T extends Persistent<ID>> int deleteByExample(Class<T> elementType,
      Example example) {
    SQLBindings sqlBindings = SqlBuilder.deleteByExample(elementType, example);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", key = "#p3")
  @JdbcCache("#p0.getClass().getSimpleName()")
  public <ID> int updateById(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields, ID id) {
    SQLBindings sqlBindings = SqlBuilder.updateById(persistent, addOrSub, nullFields, id);
    if (sqlBindings == null) {
      return 0;
    }
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  @CacheEvict(cacheNames = "JdbcCache", allEntries = true)
  @JdbcCache("#p0.getClass().getSimpleName()")
  public <ID> int updateByExample(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields,
      Example example) {
    SQLBindings sqlBindings = SqlBuilder.updateByExample(persistent, addOrSub, nullFields, example);
    if (sqlBindings == null) {
      return 0;
    }
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
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
  @CacheEvict(cacheNames = "JdbcCache", key = "#p1")
  @JdbcCache("#p0.getClass().getSimpleName()")
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
  @JdbcCache("#p0.getClass().getSimpleName()")
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
    SQLBindings sqlBindings = SqlBuilder.findByExample(elementType, example);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(sqlBindings.sql(), sqlBindings.bindings().toArray(),
        BeanPropertyRowMapper.newInstance(elementType));
  }

  @Override
  public <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType, Example example,
      int start, int limit) {
    SQLBindings sqlBindings = SqlBuilder.findByExample(elementType, example, start, limit);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(sqlBindings.sql(), sqlBindings.bindings().toArray(),
        BeanPropertyRowMapper.newInstance(elementType));
  }

  @Override
  public <ID, T extends Persistent<ID>> int countByExample(Class<T> elementType,
      Example example) {
    SQLBindings sqlBindings = SqlBuilder.countByExample(elementType, example);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate
        .queryForObject(sqlBindings.sql(), Integer.class, sqlBindings.bindings().toArray());
  }

}
