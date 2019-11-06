package com.github.edgar615.spring.lock;

import java.io.Closeable;

public interface DistributedLock extends Closeable {

  boolean acquire();

  boolean release();

  @Override
  default void close() {
    release();
  }
}
