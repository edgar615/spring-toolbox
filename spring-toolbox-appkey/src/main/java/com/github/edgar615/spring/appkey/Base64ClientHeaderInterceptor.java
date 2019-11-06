package com.github.edgar615.spring.appkey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Map;

/**
 * 从请求头中解析用base64编码后的对应的Client的JSON对象.
 */
public class Base64ClientHeaderInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(Base64ClientHeaderInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler) throws Exception {
    if ("options".equalsIgnoreCase(request.getMethod())) {
      return super.preHandle(request, response, handler);
    }

    if (handler instanceof ResourceHttpRequestHandler) {
      return super.preHandle(request, response, handler);
    }

    ClientInfo clientInfo = extractClientInfo(request);
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

  private ClientInfo extractClientInfo(HttpServletRequest request) {
    String appKeyHeader = request.getHeader("X-Client-AppKey");
    if (Strings.isNullOrEmpty(appKeyHeader)) {
      return null;
    }
    try {
      String appKeyString = new String(Base64.getDecoder().decode(appKeyHeader));
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> clientMap = mapper.readValue(appKeyString, Map.class);
      ClientInfo clientInfo = new ClientInfo();
      if (clientMap.get("appId") instanceof Long) {
        Long appId = (Long) clientMap.get("appId");
        clientInfo.setAppId(appId);
      }
      if (clientMap.get("appKey") instanceof String) {
        String appKey = (String) clientMap.get("appKey");
        clientInfo.setAppKey(appKey);
      }
      if (clientMap.get("appName") instanceof String) {
        String appName = (String) clientMap.get("appName");
        clientInfo.setAppName(appName);
      }
      clientMap.remove("appKey");
      clientMap.remove("appName");
      clientMap.forEach((k, v) -> clientInfo.addExt(k, v));
      return clientInfo;
    } catch (Exception e) {
      return null;
    }
  }
}
