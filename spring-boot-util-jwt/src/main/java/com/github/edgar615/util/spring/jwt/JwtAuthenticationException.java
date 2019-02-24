package com.github.edgar615.util.spring.jwt;

public class JwtAuthenticationException extends RuntimeException {

    public JwtAuthenticationException(String msg) {
        super(msg);
    }

    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }
}
