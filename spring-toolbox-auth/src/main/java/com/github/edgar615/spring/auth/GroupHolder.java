package com.github.edgar615.spring.auth;

import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;

public class GroupHolder {

  private static final ThreadLocal<GroupInfo> GROUP_HOLDER = new ThreadLocal<>();

  private GroupHolder() {
    throw new AssertionError("Not instantiable: " + GroupHolder.class);
  }

  public static void set(GroupInfo groupInfo) {
    GROUP_HOLDER.set(groupInfo);
  }

  public static GroupInfo get() {
    return GROUP_HOLDER.get();
  }

  public static GroupInfo getAndCheck() {
    GroupInfo principal = GROUP_HOLDER.get();
    if (principal == null) {
      throw SystemException.create(DefaultErrorCode.UNKOWN_ACCOUNT);
    }
    return principal;
  }

  public static void clear() {
    GROUP_HOLDER.remove();
  }

}
