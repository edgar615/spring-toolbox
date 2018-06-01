package com.github.edgar615.util.spring.auth;

import com.google.common.base.Strings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edgar615.util.spring.jwt.Principal;
import com.github.edgar615.util.spring.jwt.PrincipalHolder;
import com.github.edgar615.util.spring.jwt.PrincipalImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt的拦截器.
 */
@Service
public class SimpleAuthInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAuthInterceptor.class);

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

  private Principal extractPrincipal(HttpServletRequest request) {
    String principalHeader = request.getHeader("x-client-principal");
    if (Strings.isNullOrEmpty(principalHeader)) {
      return null;
    }
    try {
      String appKeyString = new String(Base64.getDecoder().decode(principalHeader));
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> principalMap = mapper.readValue(appKeyString, Map.class);
      String companyCode = (String) principalMap.get("companyCode");
      Number number = (Number) principalMap.get("userId");
      Long userId;
      if (number instanceof Long) {
        userId = (Long) number;
      } else {
        userId = number.longValue();
      }
      String username = (String) principalMap.get("username");
      String fullname = (String) principalMap.get("fullname");
      String tel = (String) principalMap.get("tel");
      String mail = (String) principalMap.get("mail");
      String jti = (String) principalMap.get("jti");
      Objects.requireNonNull(companyCode);
      Objects.requireNonNull(userId);
      PrincipalImpl principal = new PrincipalImpl();
      principal.setUserId(userId);
      principalMap.remove("userId");
      principal.setCompanyCode(companyCode);
      principalMap.remove("companyCode");
      if (!Strings.isNullOrEmpty(username)) {
        principal.setUsername(username);
        principalMap.remove("username");
      }
      if (!Strings.isNullOrEmpty(fullname)) {
        principal.setFullname(fullname);
        principalMap.remove("fullname");
      }
      if (!Strings.isNullOrEmpty(mail)) {
        principal.setMail(mail);
        principalMap.remove("mail");
      }
      if (!Strings.isNullOrEmpty(tel)) {
        principal.setTel(tel);
        principalMap.remove("tel");
      }
      if (!Strings.isNullOrEmpty(jti)) {
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