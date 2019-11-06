package com.github.edgar615.spring.distributedlock.jdbc;

public interface OrderService {

  boolean pay(String orderNo, int sleepSecond);
}
