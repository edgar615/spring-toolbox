package com.github.edgar615.util.spring.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {

  /**
   * jwt的加密密钥
   */
  private String secret;

  /**
   * 签发者
   */
  private String issuer;

  /**
   * 过期时间，单位毫秒
   */
  private long expired;

  /**
   * 面向用户
   */
  private String subject;

  /**
   * 敏感信息的加密密钥
   */
  private String sensitiveSecret;

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public long getExpired() {
    return expired;
  }

  public void setExpired(long expired) {
    this.expired = expired;
  }

  public String getSensitiveSecret() {
    return sensitiveSecret;
  }

  public void setSensitiveSecret(String sensitiveSecret) {
    this.sensitiveSecret = sensitiveSecret;
  }
}
