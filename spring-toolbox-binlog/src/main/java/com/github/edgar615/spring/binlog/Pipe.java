package com.github.edgar615.spring.binlog;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.QueryEventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Created by Edgar on 2018/5/21.
 *
 * @author Edgar  Date 2018/5/21
 */
public class Pipe {

  private final Consumer<List<DmlData>> consumer;

  private volatile boolean transcationStart = false;

//  private volatile boolean rowEvent = false;

  private volatile boolean transcationEnd = false;

  private LinkedList<DmlData> datas = new LinkedList<>();

  private List<Event> events = new ArrayList<>();

  public Pipe(Consumer<List<DmlData>> consumer) {this.consumer = consumer;}

  public void add(Event event) {
    events.add(event);
    EventType eventType = event.getHeader().getEventType();
    //. GTID event (if gtid_mode=ON) -> QUERY event with "BEGIN" as sql -> ... -> XID event |
    // QUERY event with "COMMIT" or "ROLLBACK" as sql.
    //事务的起点
    if (eventType == EventType.ANONYMOUS_GTID) {
      transcationStart = true;
    }
    //事务的结束
    if (eventType == EventType.XID) {
      transcationEnd = true;
    }
    if (eventType == EventType.QUERY) {
      QueryEventData queryEventData = event.getData();
      if (queryEventData.getSql().equalsIgnoreCase("commit")
          || queryEventData.getSql().equalsIgnoreCase("rollback")) {
        transcationEnd = true;
      }
    }
    //新增、删除、修改的开始
//    if (eventType == EventType.TABLE_MAP) {
//      rowEvent = true;
//    }
    //新增
    if (eventType == EventType.EXT_WRITE_ROWS || eventType == EventType.WRITE_ROWS) {
      composeInsert();
    }
    if (eventType == EventType.EXT_DELETE_ROWS || eventType == EventType.DELETE_ROWS) {
      composeDelete();
    }
    //修改
    if (eventType == EventType.EXT_UPDATE_ROWS || eventType == EventType.UPDATE_ROWS) {
      composeUpdate();
    }
    if (transcationStart && transcationEnd) {
      flush();
    } else if (!transcationStart) {
      flush();
    }
  }

  private Event findTableMap(List<Event> events) {
    for (int i = events.size(); i >= 0; i --) {
      Event event = events.get(i - 1);
      if (event.getData() instanceof TableMapEventData) {
        return event;
      }
    }
    throw new NoSuchElementException("TableMapEventData");
  }

  public void composeInsert() {
    Event tableMap = findTableMap(events);
    Event writeRow = events.get(events.size() - 1);
    TableMapEventData tableMapEventData = tableMap.getData();
    if (TableUtils.get(tableMapEventData.getTable()) != null) {
      InsertData insertData = new InsertData(tableMap, writeRow);
//    rowEvent = false;
      datas.add(insertData);
    }
  }

  public void composeDelete() {
    Event tableMap = findTableMap(events);
    Event deleteRow = events.get(events.size() - 1);
    TableMapEventData tableMapEventData = tableMap.getData();
    if (TableUtils.get(tableMapEventData.getTable()) != null) {
      DeleteData deleteData = new DeleteData(tableMap, deleteRow);
//    rowEvent = false;
      datas.add(deleteData);
    }

  }

  public void composeUpdate() {
    Event tableMap = findTableMap(events);
    Event updateRow = events.get(events.size() - 1);
    TableMapEventData tableMapEventData = tableMap.getData();
    if (TableUtils.get(tableMapEventData.getTable()) != null) {
      UpdateData updateData = new UpdateData(tableMap, updateRow);
//    rowEvent = false;
      datas.add(updateData);
    }

  }

  private void flush() {
    transcationStart = false;
    transcationEnd = false;
    Event event = events.get(events.size() - 1);
    //回滚不处理
    if (event.getHeader().getEventType() == EventType.QUERY) {
      QueryEventData queryEventData = event.getData();
      if (queryEventData.getSql().equalsIgnoreCase("rollback")) {
        datas.clear();
        events.clear();
        return;
      }
    }
    //入队
    try {
      consumer.accept(new ArrayList<>(datas));
    } catch (Exception e) {
      e.printStackTrace();
    }
    datas.clear();
    events.clear();

  }
}
