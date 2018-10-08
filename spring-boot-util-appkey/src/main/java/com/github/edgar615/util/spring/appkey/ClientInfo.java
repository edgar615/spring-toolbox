package com.github.edgar615.util.spring.appkey;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ClientInfo {

  private Long companyId;

  private String companyCode;

  private String appKey;

  private String appSecret;

  private String appName;

  private final Map<String, Object> ext = new HashMap<>();

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getCompanyCode() {
    return companyCode;
  }

  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  public String getAppKey() {
    return appKey;
  }

  public void setAppKey(String appKey) {
    this.appKey = appKey;
  }

  public String getAppSecret() {
    return appSecret;
  }

  public void setAppSecret(String appSecret) {
    this.appSecret = appSecret;
  }

  public void addExt(String name, Object value) {
    this.ext.put(name, value);
  }

  public Map<String, Object> ext() {
    return ImmutableMap.copyOf(ext);
  }

}