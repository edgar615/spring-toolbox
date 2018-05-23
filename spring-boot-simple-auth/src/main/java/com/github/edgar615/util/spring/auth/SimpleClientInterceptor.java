package com.github.edgar615.util.spring.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

/**
 * 从请求头中解析对应的Client.
 */
public class SimpleClientInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleClientInterceptor.class);

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

  private ClientInfo extractClientInfo(HttpServletRequest request) {
    String appKeyHeader = request.getHeader("x-client-appkey");
    if (Strings.isNullOrEmpty(appKeyHeader)) {
      return null;
    }
    try {
      String appKeyString = new String(Base64.getDecoder().decode(appKeyHeader));
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> clientMap = mapper.readValue(appKeyString, Map.class);
      String companyCode = (String) clientMap.get("companyCode");
      String appKey = (String) clientMap.get("appKey");
      String appName = (String) clientMap.get("appName");
      Objects.requireNonNull(companyCode);
      Objects.requireNonNull(appKey);
      Objects.requireNonNull(appName);
      ClientInfo clientInfo = new ClientInfo();
      clientInfo.setAppKey(appKey);
      clientInfo.setCompanyCode(companyCode);
      clientInfo.setAppName(appName);
      return clientInfo;
    } catch (Exception e) {
      return null;
    }

  }
}