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

  private String tel;

  private String mail;

  private String companyCode;

  private String jti;

  private Long companyId;

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
  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  @Override
  public String getMail() {
    return mail;
  }

  @Override
  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  @Override
  public String getCompanyCode() {
    return companyCode;
  }

  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  public void addExt(String name, Object value) {
    this.ext.put(name, value);
  }
}
