package com.github.edgar615.spring.binlog;

import com.google.common.base.CaseFormat;

import com.github.edgar615.mysql.mapping.Column;
import com.github.edgar615.mysql.mapping.ParameterType;
import com.github.edgar615.mysql.mapping.Table;
import com.github.edgar615.mysql.mapping.TableRegistry;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Edgar on 2018/5/22.
 *
 * @author Edgar  Date 2018/5/22
 */
public class TableUtils {
  public static Table getAndCheck(String tableName) {
    Optional<Table> optional =
            TableRegistry.instance().tables().stream()
                    .filter(table -> table.getName()
                            .equalsIgnoreCase(tableName))
                    .findFirst();
    if (!optional.isPresent()) {
//      SystemException exception = SystemException.create(DefaultErrorCode.TARGET_NOT_FOUND)
//              .setDetails("table:" + tableName);
      throw new NoSuchElementException("table:" + tableName);
    }
    return optional.get();
  }

  public static Table get(String tableName) {
    Optional<Table> optional =
            TableRegistry.instance().tables().stream()
                    .filter(table -> table.getName()
                            .equalsIgnoreCase(tableName))
                    .findFirst();
    return optional.orElse(null);
  }

  public static Map<String, Object> parseSource(String tableName, Map<Integer, Object> source) {
    Table table = TableUtils.getAndCheck(tableName);
    Map<String, Object> target = new HashMap<>();
    source.forEach((k, v) -> {
      Column column = table.getColumns().get(k);
      String lowCamelName
              = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL)
              .convert(column.getName().toLowerCase());
      target.put(lowCamelName, parseColumnValue(column, v));
    });
    return target;
  }

  public static Object parseColumnValue(Column column, Object value) {
    if (column.getParameterType() == ParameterType.BOOLEAN) {
      if (value == null) {
        return value;
      } else if (value.equals(1)) {
        return true;
      } else if (value.equals(0)) {
        return false;
      } else {
        return null;
      }
    }
    if (column.getParameterType() == ParameterType.INTEGER) {
      if (value == null) {
        return value;
      }
      return Integer.parseInt(value.toString());
    }
    if (column.getParameterType() == ParameterType.LONG) {
      if (value == null) {
        return value;
      }
      return Long.parseLong(value.toString());
    }
    if (column.getParameterType() == ParameterType.FLOAT) {
      if (value == null) {
        return value;
      }
      return Float.parseFloat(value.toString());
    }
    if (column.getParameterType() == ParameterType.DOUBLE) {
      if (value == null) {
        return value;
      }
      return Double.parseDouble(value.toString());
    }
    if (column.getParameterType() == ParameterType.BIGDECIMAL) {
      if (value == null) {
        return value;
      }
      return new BigDecimal(value.toString());
    }
    //todo 其他转换
    return value;
  }
}
