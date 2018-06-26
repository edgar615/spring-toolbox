package com.github.edgar615.util.spring.binlog;

import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;

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
public class DeleteData implements DmlData {

  private final List<Map<Integer, Object>> data = new ArrayList<>();

  private final String database;

  private final String table;


  public DeleteData(Event tableMap, Event deleteRow) {
    TableMapEventData tableMapEventData = tableMap.getData();
    this.database = tableMapEventData.getDatabase();
    this.table = tableMapEventData.getTable();
    DeleteRowsEventData deleteRowsEventData = deleteRow.getData();
    BitSet bitSet = deleteRowsEventData.getIncludedColumns();
    List<Serializable[]> rows = deleteRowsEventData.getRows();
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
    return DmlType.DELETE;
  }

  @Override
  public String toString() {
    return "DeleteData{" +
           "data=" + data +
           ", database='" + database + '\'' +
           ", table='" + table + '\'' +
           '}';
  }
}
