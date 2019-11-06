package com.github.edgar615.spring.eventbus;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LocalEventBusEnableAutoConfig.class)
public @interface EnableLocalEventBus {

  boolean producer() default true;

  boolean consumer() default true;
}
