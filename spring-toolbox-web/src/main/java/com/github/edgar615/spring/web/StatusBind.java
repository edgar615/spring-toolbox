package com.github.edgar615.spring.web;

import com.github.edgar615.util.exception.DefaultErrorCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Edgar on 2018/4/21.
 *
 * @author Edgar  Date 2018/4/21
 */
public class StatusBind {

  private static final Map<Integer, Integer> BIND_MAP = new HashMap<>();

  private static final StatusBind INSTANCE = new StatusBind();

  private StatusBind() {
  }

  public static StatusBind instance() {
    return INSTANCE;
  }

  public StatusBind clear() {
    BIND_MAP.clear();
    return this;
  }

  public Integer statusCode(int errorCode) {
    return BIND_MAP.getOrDefault(errorCode, 500);
  }

  public StatusBind add(Integer errorCode, Integer statusCode) {
    BIND_MAP.put(errorCode, statusCode);
    return this;
  }

  public StatusBind addDefault() {
    add(DefaultErrorCode.UNKOWN.getNumber(), 500);
    add(DefaultErrorCode.NULL.getNumber(), 500);
    add(DefaultErrorCode.UNKOWN_ACCOUNT.getNumber(), 401);
    add(DefaultErrorCode.UNKOWN_LOGIN.getNumber(), 401);
    add(DefaultErrorCode.NAME_PWD_INCORRECT.getNumber(), 400);
    add(DefaultErrorCode.PERMISSION_DENIED.getNumber(), 403);
    add(DefaultErrorCode.EXPIRE_TOKEN.getNumber(), 401);
    add(DefaultErrorCode.RESOURCE_NOT_FOUND.getNumber(), 404);
    add(DefaultErrorCode.TARGET_NOT_FOUND.getNumber(), 400);
    add(DefaultErrorCode.MISSING_ARGS.getNumber(), 400);
    add(DefaultErrorCode.INVALID_ARGS.getNumber(), 400);
    add(DefaultErrorCode.INPUT_TOO_SHORT.getNumber(), 400);
    add(DefaultErrorCode.INPUT_TOO_LONG.getNumber(), 400);
    add(DefaultErrorCode.UPLOAD_FILE_EMPTY.getNumber(), 400);
    add(DefaultErrorCode.UPLOAD_FILE_FAILED.getNumber(), 400);
    add(DefaultErrorCode.ALREADY_EXISTS.getNumber(), 400);
    add(DefaultErrorCode.TIME_OUT.getNumber(), 400);
    add(DefaultErrorCode.SERVICE_UNAVAILABLE.getNumber(), 503);
    add(DefaultErrorCode.INVALID_TYPE.getNumber(), 400);
    add(DefaultErrorCode.INVALID_SQL.getNumber(), 400);
    add(DefaultErrorCode.CAST_ERROR.getNumber(), 400);
    add(DefaultErrorCode.SERVICE_EX.getNumber(), 400);
    add(DefaultErrorCode.INVALID_TOKEN.getNumber(), 401);
    add(DefaultErrorCode.INVALID_REQ.getNumber(), 400);
    add(DefaultErrorCode.EXPIRE.getNumber(), 400);
    add(DefaultErrorCode.INVALID_JSON.getNumber(), 400);
    add(DefaultErrorCode.CONCURRENT_CONFLICT.getNumber(), 400);
    add(DefaultErrorCode.SERVICE_EXPIRED.getNumber(), 403);
    add(DefaultErrorCode.ACCOUNT_BLOCKED.getNumber(), 403);
    add(DefaultErrorCode.MISSING_HEADER.getNumber(), 428);
    add(DefaultErrorCode.TOO_MANY_REQ.getNumber(), 429);
    add(DefaultErrorCode.BREAKER_TRIPPED.getNumber(), 429);
    add(DefaultErrorCode.CONFLICT.getNumber(), 500);
    return this;
  }
}