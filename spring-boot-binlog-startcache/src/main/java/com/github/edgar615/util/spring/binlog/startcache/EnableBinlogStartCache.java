package com.github.edgar615.util.spring.binlog.startcache;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(BinlogStartCacheAutoConfig.class)
@Deprecated
public @interface EnableBinlogStartCache {
}