package com.github.edgar615.spring.distributedlock;

public interface OrderService {

  boolean pay(String orderNo, int sleepSecond);
}
