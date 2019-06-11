package com.github.edgar615.util.spring.auth;

import com.google.common.base.Strings;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 建议用户的拦截器.
 */
public class SimpleFillUserInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFillUserInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler) throws Exception {
    if ("options".equalsIgnoreCase(request.getMethod())) {
      return super.preHandle(request, response, handler);
    }

    if (handler instanceof ResourceHttpRequestHandler) {
      return super.preHandle(request, response, handler);
    }

    Principal principal = extractPrincipal(request);
    if (principal != null) {
      PrincipalHolder.set(principal);
    }
    return super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, @Nullable Exception ex) throws Exception {
    PrincipalHolder.clear();
    super.afterCompletion(request, response, handler, ex);
  }

  private Principal extractPrincipal(HttpServletRequest request) {
    String principalHeader = request.getHeader("x-client-principal");
    if (Strings.isNullOrEmpty(principalHeader)) {
      return null;
    }
    try {
      String appKeyString = new String(Base64.getDecoder().decode(principalHeader));
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> principalMap = mapper.readValue(appKeyString, Map.class);

      Number number = (Number) principalMap.get("userId");
      Long userId;
      if (number instanceof Long) {
        userId = (Long) number;
      } else {
        userId = number.longValue();
      }
      Objects.requireNonNull(userId);
      PrincipalImpl principal = new PrincipalImpl();
      principal.setUserId(userId);
      principalMap.remove("userId");
      if (principalMap.get("companyId") instanceof Long) {
        Long companyId = (Long) principalMap.get("companyId");
        principal.setCompanyId(companyId);
        principalMap.remove("companyId");
      }
      if (principalMap.get("username") instanceof String) {
        String username = (String) principalMap.get("username");
        principal.setUsername(username);
        principalMap.remove("username");
      }
      if (principalMap.get("fullname") instanceof String) {
        String fullname = (String) principalMap.get("fullname");
        principal.setFullname(fullname);
        principalMap.remove("fullname");
      }
      if (principalMap.get("mobile") instanceof String) {
        String mobile = (String) principalMap.get("mobile");
        principal.setMobile(mobile);
        principalMap.remove("mobile");
      }
      if (principalMap.get("mail") instanceof String) {
        String mail = (String) principalMap.get("mail");
        principal.setMail(mail);
        principalMap.remove("mail");
      }
      if (principalMap.get("jti") instanceof String) {
        String jti = (String) principalMap.get("jti");
        principal.setJti(jti);
        principalMap.remove("jti");
      }
      principalMap.forEach((k, v) -> principal.addExt(k, v));
      return principal;
    } catch (Exception e) {
      return null;
    }

  }
}
