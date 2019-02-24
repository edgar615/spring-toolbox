package com.github.edgar615.util.spring.jwt;

public class FailedToLoginException extends Exception {

    public FailedToLoginException(String username) {
        super(username);
    }
}
