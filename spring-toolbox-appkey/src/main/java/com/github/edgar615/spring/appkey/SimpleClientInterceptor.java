package com.github.edgar615.spring.appkey;

import com.google.common.base.Strings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 * 从请求头中解析用base64编码后的对应的Client的JSON对象.
 */
public class SimpleClientInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleClientInterceptor.class);

  private final ClientFinder clientFinder;

  public SimpleClientInterceptor(ClientFinder clientFinder) {
    this.clientFinder = clientFinder;
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

    String appKey = extractAppKey(request);
    if (Strings.isNullOrEmpty(appKey)) {
      return super.preHandle(request, response, handler);
    }
    ClientInfo clientInfo = clientFinder.findByKey(appKey);
    if (clientInfo != null) {
      ClientHolder.set(clientInfo);
    }
    return super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, @Nullable Exception ex) throws Exception {
    ClientHolder.clear();
    super.afterCompletion(request, response, handler, ex);
  }

  private String extractAppKey(HttpServletRequest request) {
    String appKey = request.getHeader("X-Client-AppKey");
    if (!Strings.isNullOrEmpty(appKey)) {
      return appKey;
    }
    appKey = request.getParameter("appKey");
    if (!Strings.isNullOrEmpty(appKey)) {
      return appKey;
    }
    return null;
  }

}
