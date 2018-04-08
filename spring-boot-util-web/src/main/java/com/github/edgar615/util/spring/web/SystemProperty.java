package com.github.edgar615.util.spring.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix="system")
public class SystemProperty {

  private Map<String, String> pages = new HashMap<>();

  private String name;

  private String copyright;

  private String poweredBy;

  private boolean sideBarShowSystem;

  public boolean isSideBarShowSystem() {
    return sideBarShowSystem;
  }

  public void setSideBarShowSystem(boolean sideBarShowSystem) {
    this.sideBarShowSystem = sideBarShowSystem;
  }


  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public String getPoweredBy() {
    return poweredBy;
  }

  public void setPoweredBy(String poweredBy) {
    this.poweredBy = poweredBy;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, String> getPages() {
    return pages;
  }

  public void setPages(Map<String, String> pages) {
    this.pages = pages;
  }
}
