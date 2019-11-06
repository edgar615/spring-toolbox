package com.github.edgar615.spring.auth;

import com.github.edgar615.spring.jwt.JwtHolder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

public class FillUserInterceptor extends HandlerInterceptorAdapter {

  private final PrincipalFinder principalFinder;

  private static final String OPTIONS = "options";

  private static final Long BACKEND_COMPANY = -1L;

  public FillUserInterceptor(PrincipalFinder principalFinder) {
    this.principalFinder = principalFinder;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (OPTIONS.equalsIgnoreCase(request.getMethod())) {
      return super.preHandle(request, response, handler);
    } else if (handler instanceof ResourceHttpRequestHandler) {
      return super.preHandle(request, response, handler);
    }
    String identifier = JwtHolder.get();
    if (identifier == null) {
      return super.preHandle(request, response, handler);
    }
    Principal principal = principalFinder.find(identifier);
    PrincipalHolder.set(principal);
    return super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, @Nullable Exception ex) throws Exception {
    PrincipalHolder.clear();
    super.afterCompletion(request, response, handler, ex);
  }
}
