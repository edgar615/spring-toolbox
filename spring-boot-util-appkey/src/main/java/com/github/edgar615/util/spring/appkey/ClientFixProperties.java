package com.github.edgar615.util.spring.appkey;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/4/22.
 */
@ConfigurationProperties(prefix = "client.fix")
@Service
public class ClientFixProperties {

  private Long appId;

  private String appKey;

  private String appSecret;

  private String appName;

  private Map<String, Object> ext;

  public Long getAppId() {
    return appId;
  }

  public void setAppId(Long appId) {
    this.appId = appId;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
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

  public Map<String, Object> getExt() {
    return ext;
  }

  public void setExt(Map<String, Object> ext) {
    this.ext = ext;
  }
}
