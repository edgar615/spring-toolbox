package com.github.edgar615.util.spring.appkey;

import com.github.edgar615.util.exception.ErrorCode;

public enum ClientError implements ErrorCode {

    NON_EXISTED_APP_KEY(8001,"Non-existed appKey"),

    UNSUPPORTED_SING_METHOD(8002,"Unsupported signMethod"),

    INCORRECT_SIGN(8003,"Incorrect sign"),

    UNKOWN_APP(8004,"Unkonwn Application"),;

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
