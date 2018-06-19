package com.github.edgar615.util.spring.auth;

import com.google.common.base.Strings;

import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;
import com.github.edgar615.util.spring.jwt.JwtProvider;
import com.github.edgar615.util.spring.jwt.Principal;
import com.github.edgar615.util.spring.jwt.PrincipalHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ou
 * 权限拦截。
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);

  private final JwtProvider jwtProvider;

  public AuthInterceptor(JwtProvider jwtProvider) {this.jwtProvider = jwtProvider;}

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler) throws Exception {
    if ("options".equalsIgnoreCase(request.getMethod())) {
      return super.preHandle(request, response, handler);
    }

    if (handler instanceof ResourceHttpRequestHandler) {
      return super.preHandle(request, response, handler);
    }
    Principal principal = null;
    try {
      String token = extractToken(request);
      if (!Strings.isNullOrEmpty(token)) {
        principal = jwtProvider.decodeUser(token);
      }
    } catch (Exception e) {
      throw e;
    }
    if (principal != null) {
      PrincipalHolder.set(principal);
    }
    return super.preHandle(request, response, handler);
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