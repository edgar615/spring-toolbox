package com.github.edgar615.util.spring.cache.binlog;

import com.github.edgar615.mysql.mapping.Table;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/5/21.
 *
 * @author Edgar  Date 2018/5/21
 */
public class InsertData implements DmlData {

  private final List<Map<Integer, Object>> data = new ArrayList<>();

  private final String database;

  private final String table;


  public InsertData(Event tableMap, Event writeRow) {
    TableMapEventData tableMapEventData = tableMap.getData();
    this.database = tableMapEventData.getDatabase();
    this.table = tableMapEventData.getTable();
    Table table = TableUtils.getAndCheck(this.table);
    WriteRowsEventData writeRowsEventData = writeRow.getData();
    BitSet bitSet = writeRowsEventData.getIncludedColumns();
    List<Serializable[]> rows = writeRowsEventData.getRows();
    for (Serializable[] serializables : rows) {
      Map<Integer, Object> rowMap = new HashMap<>();
      data.add(rowMap);
      for (int i = 0; i < bitSet.size(); i++) {
        if (bitSet.get(i)) {
          rowMap.put(i, serializables[i]);
        }
      }
    }

  }

  public String database() {
    return database;
  }

  public String table() {
    return table;
  }

  public List<Map<Integer, Object>> data() {
    return data;
  }

  @Override
  public DmlType type() {
    return DmlType.INSERT;
  }

  @Override
  public String toString() {
    return "InsertData{" +
           "data=" + data +
           ", database='" + database + '\'' +
           ", table='" + table + '\'' +
           '}';
  }
}
