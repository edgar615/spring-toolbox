package com.github.edgar615.util.spring.operatelog;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解.
 * 从别的地方借鉴的. https://github.com/t1/logging-interceptor.git
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface UserOperate {

  //操作内容
  String action() default "";

  String module() default "";
}