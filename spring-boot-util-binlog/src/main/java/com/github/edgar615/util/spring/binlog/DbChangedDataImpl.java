package com.github.edgar615.util.spring.binlog;

import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/6/26.
 *
 * @author Edgar  Date 2018/6/26
 */
class DbChangedDataImpl implements DbChangedData {

  private final String database;

  private final String table;

  private final DmlType type;

  private final List<Map<String, Object>> data;

  DbChangedDataImpl(String database, String table, DmlType type, List<Map<String, Object>> data) {
    this.database = database;
    this.table = table;
    this.type = type;
    this.data = data;
  }

  @Override
  public String database() {
    return database;
  }

  @Override
  public String table() {
    return table;
  }

  @Override
  public List<Map<String, Object>> data() {
    return data;
  }

  @Override
  public DmlType type() {
    return type;
  }
}
