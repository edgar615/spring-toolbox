package com.github.edgar615.spring.appkey;

public interface ClientFinder {
  ClientInfo findByKey(String appKey);
}
