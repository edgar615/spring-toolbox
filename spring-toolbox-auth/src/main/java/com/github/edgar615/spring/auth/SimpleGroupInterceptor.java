package com.github.edgar615.spring.auth;

import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;
import com.github.edgar615.spring.jwt.AESUtils;
import com.github.edgar615.spring.jwt.JwtHolder;
import com.google.common.base.Strings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 * 从请求头中解析出加密后的群组
 */
public class SimpleGroupInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleGroupInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response, Object handler) throws Exception {
    if ("options".equalsIgnoreCase(request.getMethod())) {
      return super.preHandle(request, response, handler);
    }

    if (handler instanceof ResourceHttpRequestHandler) {
      return super.preHandle(request, response, handler);
    }

    String groupKey = extractGroupKey(request);
    if (Strings.isNullOrEmpty(groupKey)) {
      return super.preHandle(request, response, handler);
    }
    String userIdentifier = JwtHolder.get();
    if (Strings.isNullOrEmpty(userIdentifier)) {
      return super.preHandle(request, response, handler);
    }
    String groupIdStr = null;
    try {
      groupIdStr = AESUtils.decrypt(groupKey, userIdentifier);
    } catch (Exception e) {
      throw SystemException.create(DefaultErrorCode.INVALID_ARGS)
          .setDetails("invalid groupKey");
    }
    GroupInfoImpl groupInfo = new GroupInfoImpl();
    groupInfo.setGroupId(Long.parseLong(groupIdStr));
    GroupHolder.set(groupInfo);
    return super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, @Nullable Exception ex) throws Exception {
    GroupHolder.clear();
    super.afterCompletion(request, response, handler, ex);
  }

  private String extractGroupKey(HttpServletRequest request) {
    String groupKey = request.getHeader("X-Group-Key");
    if (!Strings.isNullOrEmpty(groupKey)) {
      return groupKey;
    }
    groupKey = request.getParameter("groupKey");
    if (!Strings.isNullOrEmpty(groupKey)) {
      return groupKey;
    }
    return null;
  }

}
