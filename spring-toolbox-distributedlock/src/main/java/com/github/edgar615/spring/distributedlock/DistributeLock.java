package com.github.edgar615.spring.distributedlock;

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
     * 锁的存储空间，对于redis来说，这个就是redis key的前缀，对于数据库来说这个值是数据库表名
     *
     * 支持spring EL表达式，便于DB在高并发时候对热点资源进行拆分，redis锁一般用不到
     * @return
     */
    String storeName() default "distributed_lock";

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
