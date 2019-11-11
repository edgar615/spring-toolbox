package com.github.edgar615.spring.cache;

import java.util.List;
import java.util.Map;

public class StartCacheChangedData {

  private final String name;

  private final ChangedType type;

  private final List<Map<String, Object>> data;

  public StartCacheChangedData(String name, ChangedType type,
      List<Map<String, Object>> data) {
    this.name = name;
    this.type = type;
    this.data = data;
  }

  public String getName() {
    return name;
  }

  public ChangedType getType() {
    return type;
  }

  public List<Map<String, Object>> getData() {
    return data;
  }
}
