package com.github.edgar615.spring.mybatis;

import com.github.edgar615.util.base.StringUtils;
import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.db.SQLBindings;
import com.github.edgar615.util.search.Criterion;
import com.github.edgar615.util.search.Example;
import com.github.edgar615.util.search.Op;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.ibatis.annotations.Param;

public class SysUserSqlProvider {


  private static final String REVERSE_KEY = "-";

  public <ID> String insert(Persistent<ID> persistent){
    Map<String, Object> map = persistent.toMap();
    List<String> columns = new ArrayList<>();
    List<String> prepare = new ArrayList<>();
    List<Object> params = new ArrayList<>();
    List<String> virtualFields = persistent.virtualFields();
    map.forEach((k, v) -> {
      if (v != null && !virtualFields.contains(k)) {
        columns.add(underscoreName(k));
        prepare.add("#{" + k + "}");
        params.add(v);
      }
    });

    String tableName = underscoreName(persistent.getClass().getSimpleName());
    StringBuilder s = new StringBuilder();
    s.append("insert into ")
        .append(tableName)
        .append("(")
        .append(Joiner.on(",").join(columns))
        .append(") values(")
        .append(Joiner.on(",").join(prepare))
        .append(")");
    return SQLBindings.create(s.toString(), params).sql();

  }

  /**
   * 根据主键删除.
   *
   * @param id 主键
   * @return SQL
   */
  public static String deleteById(Long id) {
    return "delete from sys_user where sys_user_id = #{id}";
  }

  /**
   * 根据主键查询.
   *
   * @param id 主键
   * @return SQL
   */
  public static String findById(Long id) {
    return "select " + selectFields(new SysUser(), new ArrayList<>()) + " from sys_user where sys_user_id = #{id}";
  }

  /**
   * 根据主键查询.
   *
   * @param id 主键
   * @param fields 返回的属性列表
   * @return SQL
   */
  public static String findCustomFieldById(@Param("id") Long id, @Param("fields") List<String> fields) {
    return "select " + selectFields(new SysUser(), fields) + " from sys_user where sys_user_id = #{id}";
  }

  /**
   * 根据条件查询.
   *
   * @return SQL
   */
  public static String findByExample(   @Param("example")   Example example) {
    example = removeUndefinedField(SysUser.class, example);
    SysUser domain = new SysUser();
    String selectedField = selectFields(domain, example.fields());
    StringBuilder sql = new StringBuilder();
    sql.append("select ");
    if (example.isDistinct()) {
      sql.append("distinct ");
    }
    sql.append(selectedField)
        .append(" from ")
        .append(underscoreName(SysUser.class.getSimpleName()));
    SQLBindings sqlBindings = whereSql(example.criteria());
    if (!Strings.isNullOrEmpty(sqlBindings.sql())) {
      sql.append(" where ").append(sqlBindings.sql());
    }
    if (!example.orderBy().isEmpty()) {
      sql.append(orderSql(example.orderBy()));
    }
    return sql.toString();
  }

  /**
   * 根据条件查询.
   *
   * @param elementType 持久化对象 * @param example 返回的属性列表
   * @param offset offset
   * @param limit limit
   * @param <ID> 主键类型
   * @return SQL
   */
//  public static <ID> SQLBindings findByExample(Class<? extends Persistent<ID>> elementType,
//      Example example, long offset, long limit) {
//    SQLBindings sqlBindings = findByExample(elementType, example);
//    StringBuilder sql = new StringBuilder(sqlBindings.sql());
//    sql.append(" limit ?, ?");
//    List<Object> args = new ArrayList<>(sqlBindings.bindings());
//    args.add(offset);
//    args.add(limit);
//    return SQLBindings.create(sql.toString(), args);
//  }

  /**
   * 根据条件查询总数.
   *
   * @param elementType 持久化对象 * @param example 返回的属性列表
   * @param <ID> 主键类型
   * @return SQL
   */
  public static <ID> SQLBindings countByExample(Class<? extends Persistent<ID>> elementType,
      Example example) {
    example = removeUndefinedField(elementType, example);
    StringBuilder sql = new StringBuilder();
    sql.append("select ");
    if (example.isDistinct()) {
      Persistent<ID> domain = Persistent.create(elementType);
      String selectedField = selectFields(domain, example.fields());
      sql.append("count(distinct(").append(selectedField).append("))");
    } else {
      sql.append("count(*)");
    }
    sql.append(" from ")
        .append(underscoreName(elementType.getSimpleName()));
    SQLBindings sqlBindings = whereSql(example.criteria());
    if (!Strings.isNullOrEmpty(sqlBindings.sql())) {
      sql.append(" where ").append(sqlBindings.sql());
    }
    return SQLBindings.create(sql.toString(), sqlBindings.bindings());
  }

  /**
   * 根据条件删除.
   *
   * @param elementType 持久化对象
   * @param example 条件
   * @param <ID> 主键类型
   * @return SQL
   */
  public static <ID> String deleteByExample(Class<? extends Persistent<ID>> elementType,
      Example example) {
    example = removeUndefinedField(elementType, example);
    SQLBindings sqlBindings = whereSql(example.criteria());
    String tableName = StringUtils.underscoreName(elementType.getSimpleName());
    StringBuilder sql = new StringBuilder("delete from ")
        .append(tableName);
    if (!example.criteria().isEmpty()) {
      sql.append(" where ")
          .append(sqlBindings.sql());
    }

    return sql.toString();
  }

  /**
   * 根据主键更新,忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @return SQL
   */
  public static <ID> SQLBindings updateById(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields, ID id) {
    Example example = Example.create().equalsTo(persistent.primaryField(), id);
    return updateByExample(persistent, addOrSub, nullFields, example);
  }

  /**
   * 根据条件更新,忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param example 条件
   * @param <ID> 主键类型
   * @return SQL
   */
  public static <ID> SQLBindings updateByExample(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields, Example example) {
    boolean noUpdated = persistent.toMap().values().stream()
        .allMatch(v -> v == null);
    boolean noAddOrSub = addOrSub == null
        || addOrSub.keySet().stream().allMatch(v -> !persistent.fields().contains(v));
    boolean noNull = nullFields == null
        || nullFields.stream().allMatch(v -> !persistent.fields().contains(v));
    if (noUpdated && noAddOrSub && noNull) {
      return null;
    }

    //对example做一次清洗，将表中不存在的条件删除，避免频繁出现500错误
    example = example.removeUndefinedField(persistent.fields());
    Map<String, Object> map = persistent.toMap();
    List<String> columns = new ArrayList<>();
    List<Object> params = new ArrayList<>();
    //忽略虚拟列
    List<String> virtualFields = persistent.virtualFields();
    map.forEach((k, v) -> {
      if (v != null && !virtualFields.contains(k)) {
        columns.add(StringUtils.underscoreName(k) + " = ?");
        params.add(v);
      }
    });
    if (addOrSub != null) {
      for (Map.Entry<String, Number> entry : addOrSub.entrySet()) {
        String key = entry.getKey();
        if (persistent.fields().contains(key)) {
          String underscoreKey = StringUtils.underscoreName(key);
          BigDecimal value = new BigDecimal(entry.getValue().toString());
          if (value.compareTo(new BigDecimal(0)) > 0) {
            columns.add(
                underscoreKey + " = " + underscoreKey + " + " + value);
          } else {
            //int 以前这样取反的~(entry.getValue() - 1)
            columns.add(
                underscoreKey + " = " + underscoreKey + " - " + value.negate());
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
      return null;
    }

    String tableName = StringUtils.underscoreName(persistent.getClass().getSimpleName());
    StringBuilder sql = new StringBuilder();
    sql.append("update ")
        .append(tableName)
        .append(" set ")
        .append(Joiner.on(",").join(columns));
    List<Object> args = new ArrayList<>(params);
    if (!example.criteria().isEmpty()) {
      SQLBindings sqlBindings = whereSql(example.criteria());
      sql.append(" where ")
          .append(sqlBindings.sql());
      args.addAll(sqlBindings.bindings());
    }
    return SQLBindings.create(sql.toString(), args);
  }

  private static <ID> Example primaryKeyExample(Class<? extends Persistent<ID>> elementType,
      ID id) {
    Persistent<ID> domain = Persistent.create(elementType);
    return Example.create().equalsTo(domain.primaryField(), id);
  }

  private static String orderSql(List<String> orderBy) {
    if (orderBy.isEmpty()) {
      return "";
    }
    List<String> sql = orderBy.stream()
        .distinct()
        .map(o -> {
          if (o.startsWith(REVERSE_KEY)) {
            return underscoreName(o.substring(1)) + " desc";
          }
          return underscoreName(o);
        }).collect(Collectors.toList());
    return " order by " + Joiner.on(",").join(sql);
  }

  private static SQLBindings whereSql(List<Criterion> criteria) {
    if (criteria.isEmpty()) {
      return SQLBindings.create("", new ArrayList<>());
    }
    List<String> sql = new ArrayList<>();
    List<Object> bindings = new ArrayList<>();
    criteria
        .forEach(c -> addSqlFromCriterion(sql, bindings, c));
    return SQLBindings.create(Joiner.on(" and ").join(sql), bindings);
  }

  private static <ID, T extends Persistent<ID>> List<String> removeUndefinedColumn(
      Class<T> elementType,
      List<String> fields) {
    Persistent<ID> persistent = Persistent.create(elementType);
    List<String> domainColumns = persistent.fields();
    return fields.stream()
        .filter(f -> domainColumns.contains(f))
        .collect(Collectors.toList());
  }

  private static <ID, T extends Persistent<ID>> Example removeUndefinedField(Class<T> elementType,
      Example example) {
    //对example做一次清洗，将表中不存在的条件删除，避免频繁出现500错误
    Persistent<ID> persistent = Persistent.create(elementType);
    example = example.removeUndefinedField(persistent.fields());
    return example;
  }

  private static void addSqlFromCriterion(List<String> sql, List<Object> bindings,
      Criterion criterion) {
    SQLBindings sqlBindings = criterion(criterion);
    sql.add(sqlBindings.sql());
    bindings.addAll(sqlBindings.bindings());
  }

  private static SQLBindings criterion(Criterion criterion) {
    StringBuilder sql = new StringBuilder();
    List<Object> bindings = new ArrayList<>();

    if (criterion.op() == Op.IS_NULL) {
      sql.append(underscoreName(criterion.field())).append(" is null");
    }
    if (criterion.op() == Op.IS_NOT_NULL) {
      sql.append(underscoreName(criterion.field())).append(" is not null");
    }
    if (criterion.op() == Op.EQ) {
      sql.append(underscoreName(criterion.field())).append(" = ");
      sql.append("#{criterion.value()}");
    }
    if (criterion.op() == Op.NE) {
      sql.append(underscoreName(criterion.field())).append(" <> ");
      sql.append("#{criterion.value()}");
    }
    if (criterion.op() == Op.GT) {
      sql.append(underscoreName(criterion.field())).append(" > ");
      sql.append("#{criterion.value()}");
    }
    if (criterion.op() == Op.GE) {
      sql.append(underscoreName(criterion.field())).append(" >= ");
      sql.append("#{criterion.value()}");
    }
    if (criterion.op() == Op.LT) {
      sql.append(underscoreName(criterion.field())).append(" < ");
      sql.append("#{criterion.value()}");
    }
    if (criterion.op() == Op.LE) {
      sql.append(underscoreName(criterion.field())).append(" <= ");
      sql.append("#{criterion.value()}");
    }
    if (criterion.op() == Op.SW) {
      sql.append(underscoreName(criterion.field())).append(" like ?");
      sql.append("#{" + criterion.field() + "}");
//      bindings.add(criterion.value() + "%");
    }
    if (criterion.op() == Op.EW) {
      sql.append(underscoreName(criterion.field())).append(" like ?");
      sql.append("#{" + criterion.field() + "}");
//      bindings.add("%" + criterion.value());
    }
    if (criterion.op() == Op.CN) {
      sql.append(underscoreName(criterion.field())).append(" like ?");
      sql.append("#{" + criterion.field() + "}");
//      bindings.add("%" + criterion.value() + "%");
    }
    if (criterion.op() == Op.BETWEEN) {
      sql.append(underscoreName(criterion.field())).append(" between ");
      sql.append("#{criterion.value()}").append(" and ").append("#{criterion.secondValue()}");
    }
    if (criterion.op() == Op.IN) {
      List<Object> values = (List<Object>) criterion.value();
      List<String> strings = values.stream()
          .map(v -> "?")
          .collect(Collectors.toList());
      sql.append(underscoreName(criterion.field()) + " in ("
          + Joiner.on(",").join(strings)
          + ")");
      bindings.addAll(values);
    }
    if (criterion.op() == Op.NOT_IN) {
      List<Object> values = (List<Object>) criterion.value();
      List<String> strings = values.stream()
          .map(v -> "?")
          .collect(Collectors.toList());
      sql.append(underscoreName(criterion.field())).append(" not in (")
          .append(Joiner.on(",").join(strings)).append(")");
      bindings.addAll(values);
    }
    return SQLBindings.create(sql.toString(), bindings);
  }

  private static <ID> String selectFields(Persistent<ID> domain, List<String> fields) {
    String selectedField;
    List<String> domainFields = domain.fields();
    List<String> filterFields = fields.stream().filter(f -> domainFields.contains(f))
        .collect(Collectors.toList());
    if (filterFields.isEmpty()) {
      selectedField = Joiner.on(", ")
          .join(domainFields.stream()
              .map(f -> underscoreName(f))
              .collect(Collectors.toList()));
    } else {
      selectedField = Joiner.on(", ")
          .join(filterFields.stream()
              .map(f -> underscoreName(f))
              .collect(Collectors.toList()));
    }
    return selectedField;
  }

  private static String underscoreName(String name) {
    return StringUtils.underscoreName(name);
  }
}
