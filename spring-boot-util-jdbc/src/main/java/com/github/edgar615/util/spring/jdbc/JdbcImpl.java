package com.github.edgar615.util.spring.jdbc;

import com.google.common.base.Joiner;

import com.github.edgar615.util.base.MorePreconditions;
import com.github.edgar615.util.base.StringUtils;
import com.github.edgar615.util.db.Jdbc;
import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.db.SQLBindings;
import com.github.edgar615.util.db.SqlBuilder;
import com.github.edgar615.util.search.Example;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;

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
    example = removeUndefinedField(elementType, example);

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    SQLBindings sqlBindings = SqlBuilder.whereSql(example.criteria());
    String tableName = StringUtils.underscoreName(elementType.getSimpleName());
    String sql = "delete from "
                 + tableName;
    if (!example.criteria().isEmpty()) {
      sql += " where " + sqlBindings.sql();
    }
    return jdbcTemplate.update(sql, sqlBindings.bindings().toArray());
  }

  @Override
  public <ID> int updateById(Persistent<ID> persistent,
                                                       Map<String, Integer> addOrSub,
                                                       List<String> nullFields, ID id) {
    boolean noUpdated = persistent.toMap().values().stream()
            .allMatch(v -> v == null);
    boolean noAddOrSub = addOrSub.keySet().stream()
            .allMatch(v -> !persistent.fields().contains(v));
    boolean noNull = nullFields.stream()
            .allMatch(v -> !persistent.fields().contains(v));
    if (noUpdated && noAddOrSub && noNull) {
      return 0;
    }
    SQLBindings sqlBindings = SqlBuilder.updateById(persistent, addOrSub, nullFields, id);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  public <ID> int updateByExample(Persistent<ID> persistent,
                                                            Map<String, Integer> addOrSub,
                                                            List<String> nullFields,
                                                            Example example) {
    //对example做一次清洗，将表中不存在的条件删除，避免频繁出现500错误
    example = example.removeUndefinedField(persistent.fields());
    Map<String, Object> map = persistent.toMap();
    List<String> columns = new ArrayList<>();
    List<Object> params = new ArrayList<>();
    List<String> virtualFields = persistent.virtualFields();
    map.forEach((k, v) -> {
      if (v != null && !virtualFields.contains(k)) {
        columns.add(StringUtils.underscoreName(k) + " = ?");
        params.add(v);
      }
    });
    if (addOrSub != null) {
      for (Map.Entry<String, Integer> entry : addOrSub.entrySet()) {
        String key = entry.getKey();
        if (persistent.fields().contains(key)) {
          String underScoreKey = StringUtils.underscoreName(key);
          if (entry.getValue() > 0) {
            columns.add(underScoreKey + " = " + underScoreKey + " + " + entry.getValue());
          } else {
            columns.add(
                    underScoreKey + " = " + underScoreKey + " - " + entry.getValue());
          }
        }
      }
    }
    if (nullFields != null) {
      List<String> nullColumns = nullFields.stream()
              .filter(f -> persistent.fields().contains(f))
              .map(f -> StringUtils.underscoreName(f))
              .map(f -> f + " = null")
              .collect(Collectors.toList());
      columns.addAll(nullColumns);
    }

    if (columns.isEmpty()) {
      return 0;
    }
    MorePreconditions.checkNotEmpty(columns, "no update field");

    String tableName = StringUtils.underscoreName(persistent.getClass().getSimpleName());
    StringBuilder sql = new StringBuilder();
    sql.append("update ")
            .append(tableName)
            .append(" set ")
            .append(Joiner.on(",").join(columns));
    List<Object> args = new ArrayList<>(params);
    if (!example.criteria().isEmpty()) {
      SQLBindings sqlBindings = SqlBuilder.whereSql(example.criteria());
      sql.append(" where ")
              .append(sqlBindings.sql());
      args.addAll(sqlBindings.bindings());
    }
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sql.toString(), args.toArray());
  }

  @Override
  public <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id,
                                                   List<String> fields) {
    Persistent<ID> persistent = newDomain(elementType);
    fields.removeIf(f -> persistent.fields().contains(f));
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
    example = removeUndefinedField(elementType, example);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    SQLBindings sqlBindings = SqlBuilder.whereSql(example.criteria());
    String tableName = StringUtils.underscoreName(elementType.getSimpleName());
    String sql = "select *  from "
                 + tableName;
    if (!example.criteria().isEmpty()) {
      sql += " where " + sqlBindings.sql();
    } else {
      sql += "  " + sqlBindings.sql();
    }
    if (!example.orderBy().isEmpty()) {
      sql += SqlBuilder.orderSql(example.orderBy());
    }
    return jdbcTemplate.query(sql, sqlBindings.bindings().toArray(),
                              BeanPropertyRowMapper.newInstance(elementType));
  }

  @Override
  public <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType, Example example,
                                                              int start, int limit) {
    example = removeUndefinedField(elementType, example);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    SQLBindings sqlBindings = SqlBuilder.whereSql(example.criteria());
    String tableName = StringUtils.underscoreName(elementType.getSimpleName());
    String sql = "select *  from "
                 + tableName;
    if (!example.criteria().isEmpty()) {
      sql += " where " + sqlBindings.sql();
    } else {
      sql += "  " + sqlBindings.sql();
    }
    if (!example.orderBy().isEmpty()) {
      sql += SqlBuilder.orderSql(example.orderBy());
    }
    sql += " limit ?, ?";
    List<Object> args = new ArrayList<>(sqlBindings.bindings());
    args.add(start);
    args.add(limit);

    return jdbcTemplate.query(sql, args.toArray(),
                              BeanPropertyRowMapper.newInstance(elementType));
  }

  @Override
  public <ID, T extends Persistent<ID>> int countByExample(Class<T> elementType,
                                                           Example example) {
    example = removeUndefinedField(elementType, example);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    SQLBindings sqlBindings = SqlBuilder.whereSql(example.criteria());
    String tableName = StringUtils.underscoreName(elementType.getSimpleName());
    String sql = "select count(*) from "
                 + tableName;
    if (!example.criteria().isEmpty()) {
      sql += " where " + sqlBindings.sql();
    } else {
      sql += "  " + sqlBindings.sql();
    }
    List<Object> args = new ArrayList<>(sqlBindings.bindings());

    return jdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
  }

  private <ID, T extends Persistent<ID>> List<String> removeUndefinedColumn(Class<T> elementType,
                                                                            List<String> fields) {
    Persistent<ID> persistent = newDomain(elementType);
    List<String> domainColumns = persistent.fields();
    return fields.stream()
            .filter(f -> domainColumns.contains(f))
            .collect(Collectors.toList());
  }

  private <ID, T extends Persistent<ID>> Example removeUndefinedField(Class<T> elementType,
                                                                      Example example) {
    //对example做一次清洗，将表中不存在的条件删除，避免频繁出现500错误
    Persistent<ID> persistent = newDomain(elementType);
    example = example.removeUndefinedField(persistent.fields());
    return example;
  }

  private <ID> Persistent newDomain(Class<? extends Persistent<ID>> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
