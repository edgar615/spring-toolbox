package com.github.edgar615.util.spring.auth;

import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;

public class ClientHolder {

  private static final ThreadLocal<ClientInfo> CLIENT_HOLDER = new ThreadLocal<>();

  private ClientHolder() {
    throw new AssertionError("Not instantiable: " + ClientHolder.class);
  }

  public static void set(ClientInfo principal) {
    CLIENT_HOLDER.set(principal);
  }

  public static ClientInfo get() {
    return CLIENT_HOLDER.get();
  }

  public static ClientInfo getAndCheck() {
    ClientInfo clientInfo = CLIENT_HOLDER.get();
    if (clientInfo == null) {
      throw SystemException.create(DefaultErrorCode.UNKOWN_ACCOUNT);
    }
    return clientInfo;
  }

  public static void clear() {
    CLIENT_HOLDER.remove();
  }

}