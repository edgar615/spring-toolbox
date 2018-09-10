package com.github.edgar615.util.spring.jdbc;

import com.github.edgar615.util.base.StringUtils;
import com.github.edgar615.util.db.Jdbc;
import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.db.SQLBindings;
import com.github.edgar615.util.db.SqlBuilder;
import com.github.edgar615.util.search.Example;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Created by Edgar on 2017/8/8.
 *
 * @author Edgar  Date 2017/8/8
 */
public class JdbcImpl implements Jdbc {

  private final DataSource dataSource;

  public JdbcImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public <ID> void insert(Persistent<ID> persistent) {
    SQLBindings sqlBindings = SqlBuilder.insert(persistent);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  public <ID> void insertAndGeneratedKey(Persistent<ID> persistent) {
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
  }

  @Override
  public <ID, T extends Persistent<ID>> int deleteById(Class<T> elementType, ID id) {
    SQLBindings sqlBindings = SqlBuilder.deleteById(elementType, id);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  public <ID, T extends Persistent<ID>> int deleteByExample(Class<T> elementType,
      Example example) {
    SQLBindings sqlBindings = SqlBuilder.deleteByExample(elementType, example);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  public <ID> int updateById(Persistent<ID> persistent,
      Map<String, Integer> addOrSub,
      List<String> nullFields, ID id) {
    SQLBindings sqlBindings = SqlBuilder.updateById(persistent, addOrSub, nullFields, id);
    if (sqlBindings == null) {
      return 0;
    }
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  public <ID> int updateByExample(Persistent<ID> persistent,
      Map<String, Integer> addOrSub,
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
    SQLBindings sqlBindings = SqlBuilder.findById(elementType, id, fields);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    List<T> result =
        jdbcTemplate.query(sqlBindings.sql(), sqlBindings.bindings().toArray(),
            BeanPropertyRowMapper.newInstance(elementType));
    if (result.isEmpty()) {
      return null;
    }
    return result.get(0);

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
