package com.github.edgar615.util.spring.appkey;

public interface ClientFinder {
  ClientInfo findByKey(String appKey);
}
