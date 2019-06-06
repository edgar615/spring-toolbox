package com.github.edgar615.util.spring.jwt;

public class Token {

  private String token;

  private long expire;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public long getExpire() {
    return expire;
  }

  public void setExpire(long expire) {
    this.expire = expire;
  }
}
