package com.github.edgar615.util.spring.auth;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/13.
 */
public interface Principal {

  Long getUserId();

  String getUsername();

  String getFullname();

  String getMobile();

  String getMail();

  String getJti();

  Map<String, Object> ext();
}
