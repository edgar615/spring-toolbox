package com.github.edgar615.util.spring.jwt;


import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;

public class JwtHolder {

  private static final ThreadLocal<String> PRINCIPAL_HOLDER = new ThreadLocal<>();

  private JwtHolder() {
    throw new AssertionError("Not instantiable: " + JwtHolder.class);
  }

  public static void set(String principal) {
    PRINCIPAL_HOLDER.set(principal);
  }

  public static String get() {
    return PRINCIPAL_HOLDER.get();
  }

  public static String getAndCheck() {
    String principal = PRINCIPAL_HOLDER.get();
    if (principal == null) {
      throw SystemException.create(DefaultErrorCode.UNKOWN_ACCOUNT);
    }
    return principal;
  }

  public static void clear() {
    PRINCIPAL_HOLDER.remove();
  }

}
