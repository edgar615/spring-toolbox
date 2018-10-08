package com.github.edgar615.util.spring.appkey;

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

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, @Nullable Exception ex) throws Exception {
    ClientHolder.clear();
    super.afterCompletion(request, response, handler, ex);
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
      ClientInfo clientInfo = new ClientInfo();
      if (clientMap.get("companyCode") instanceof String) {
        String companyCode = (String) clientMap.get("companyCode");
        clientInfo.setCompanyCode(companyCode);
      }
      if (clientMap.get("companyId") instanceof Long) {
        Long companyId = (Long) clientMap.get("companyId");
        clientInfo.setCompanyId(companyId);
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
      clientMap.remove("companyId");
      clientMap.remove("companyCode");
      clientMap.forEach((k, v) -> clientInfo.addExt(k, v));
      return clientInfo;
    } catch (Exception e) {
      return null;
    }

  }
}