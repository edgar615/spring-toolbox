package com.github.edgar615.util.spring.appkey;

import com.github.edgar615.util.exception.ErrorCode;

public enum ClientError implements ErrorCode {

    NON_EXISTED_APP_KEY(9001,"Non-existed appKey"),

    UNSUPPORTED_SING_METHOD(9002,"Unsupported signMethod"),

    INCORRECT_SIGN(9003,"Incorrect sign");

    private final int code;

    private final String message;

    private ClientError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getNumber() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}