package com.github.edgar615.util.spring.auth;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/13.
 */
public class PrincipalImpl implements Principal {

  private Long userId;

  private String username;

  private String fullname;

  private String mobile;

  private String mail;

  private String jti;

  private final Map<String, Object> ext = new HashMap<>();

  @Override
  public String getJti() {
    return jti;
  }

  @Override
  public Map<String, Object> ext() {
    return ImmutableMap.copyOf(ext);
  }

  public void setJti(String jti) {
    this.jti = jti;
  }

  @Override
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  @Override
  public String getMobile() {
    return mobile;
  }

  @Override
  public String getMail() {
    return mail;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public void addExt(String name, Object value) {
    this.ext.put(name, value);
  }
}
