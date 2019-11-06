package com.github.edgar615.spring.auth;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

public class GroupInfoImpl implements GroupInfo {

  private Long groupId;

  private String name;

  private final Map<String, Object> ext = new HashMap<>();

  @Override
  public Long getGroupId() {
    return groupId;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Map<String, Object> ext() {
    return ImmutableMap.copyOf(ext);
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void addExt(String name, Object value) {
    this.ext.put(name, value);
  }
}
