package com.github.edgar615.spring.lock;

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
     * 锁的资源。例如锁定某个订单:order:#orderno，锁定整个任务表job
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
     * 重试的间隔时间，单位毫秒，通过重试间隔和重试次数可以实现锁的等待超时
     */
    long retryIntervalMills() default 200L;

    /**
     * 重试次数，默认不重试
     */
    int retryTimes() default 0;

}