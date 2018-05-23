package com.github.edgar615.util.spring.auth;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ClientInfo {

  private String companyCode;

  private String appKey;

  private String appName;

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
}