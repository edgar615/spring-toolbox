package com.github.edgar615.util.spring.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {

  private String secret;

  private String issuer;

  private long expired;

  private String subject;

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

}
