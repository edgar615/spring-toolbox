package com.github.edgar615.util.spring.web;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 没起作用，还有继续研究.
 *
 * @author Edgar  Date 2018/6/27
 */
@Deprecated
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ApiBasePathEnableAutoConfig.class)
public @interface EnableApiBasePath {
  String value() default "api";
}