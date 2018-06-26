package com.github.edgar615.util.spring.binlog;

import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/5/22.
 *
 * @author Edgar  Date 2018/5/22
 */
public interface DbChangedData {
  String database();

  String table();

  List<Map<String, Object>> data();

  DmlType type();

  static DbChangedData create(String database, String table, DmlType type,
                              List<Map<String, Object>> data) {
    return new DbChangedDataImpl(database, table, type, data);
  }
}
