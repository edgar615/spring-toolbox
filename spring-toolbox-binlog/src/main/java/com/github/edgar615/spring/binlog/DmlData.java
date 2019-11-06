package com.github.edgar615.spring.binlog;

import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/5/22.
 *
 * @author Edgar  Date 2018/5/22
 */
public interface DmlData {
  String database();

  String table();

  List<Map<Integer, Object>> data();

  DmlType type();
}
