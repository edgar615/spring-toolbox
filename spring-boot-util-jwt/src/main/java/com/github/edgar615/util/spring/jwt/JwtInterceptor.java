package com.github.edgar615.util.spring.jwt;

import com.google.common.base.Strings;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 * JWT拦截。
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtInterceptor.class);

  private final JwtProvider jwtProvider;

  public JwtInterceptor(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response, Object handler) throws Exception {
    if ("options".equalsIgnoreCase(request.getMethod())) {
      return super.preHandle(request, response, handler);
    }

    if (handler instanceof ResourceHttpRequestHandler) {
      return super.preHandle(request, response, handler);
    }
    try {
      String token = extractToken(request);
      if (!Strings.isNullOrEmpty(token)) {
        String identifier = jwtProvider.decodeToken(token);
        JwtHolder.set(identifier);
      }
    } catch (Exception e) {
      throw e;
    }
    return super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, @Nullable Exception ex) throws Exception {
    JwtHolder.clear();
    super.afterCompletion(request, response, handler, ex);
  }

  private String extractToken(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (!Strings.isNullOrEmpty(token)) {
      if (token.startsWith("Bearer ")) {
        return token.substring("Bearer ".length());
      }
    }
    token = request.getParameter("token");
    if (!Strings.isNullOrEmpty(token)) {
      return token;
    }
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("web_session_token".equalsIgnoreCase(cookie.getName())) {
          token = cookie.getValue();
          return token;
        }
      }
    }
    return null;
  }
}
