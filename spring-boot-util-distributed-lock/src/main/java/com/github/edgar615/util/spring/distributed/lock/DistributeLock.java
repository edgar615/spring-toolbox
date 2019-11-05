package com.github.edgar615.util.spring.distributed.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁的主键
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributeLock {

    /**
     * 锁的资源。
     * 支持spring El表达式
     */
    String lockKey();

    /**
     * 锁的客户端标识。
     *  支持spring El表达式
     */
    String lockValue();

    /**
     * 过期时间,单位毫秒
     */
    long expireMills() default 5000L;

    /**
     * 重试的间隔时间，单位毫秒
     */
    long retryIntervalMills() default 200L;

    /**
     * 重试次数，默认不重试
     */
    int retryTimes() default 0;

}
