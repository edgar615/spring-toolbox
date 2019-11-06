package com.github.edgar615.spring.operatelog;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Logged {

  String USE_CLASS_LOGGER = "###USE_CLASS_LOGGER###";
  String CAMEL_CASE_METHOD_NAME = "###CAMEL_CASE_METHOD_NAME###";

  /**
   * 日志级别
   * @return LogLevel
   */
  LogLevel level() default LogLevel.DEBUG;

  /**
   * The class used to create the logger
   * @return 日志名称
   */
  Class<?> logger() default void.class;

  /**
   * 日志名称，如果不想使用class的名称，默认使用logger()
   * @return 日志名称
   */
  String loggerString() default USE_CLASS_LOGGER;

  /**
   * 日志格式
   * @return 日志格式
   */
  String value() default CAMEL_CASE_METHOD_NAME;

  /**
   * 方法返回时的日志
   * @return 日志格式
   */
  String returnFormat() default "return {returnValue} [time:{time}]";
}
