package com.github.edgar615.util.spring.appkey;

import com.github.edgar615.util.base.EncryptUtils;
import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 从请求头中解析对应的Client.
 */
public class ClientInfoInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientInfoInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler) throws Exception {
    if ("options".equalsIgnoreCase(request.getMethod())) {
      return super.preHandle(request, response, handler);
    }

    if (handler instanceof ResourceHttpRequestHandler) {
      return super.preHandle(request, response, handler);
    }

    findAndCheckClient(request);
    return super.preHandle(request, response, handler);
  }

  private void findAndCheckClient(HttpServletRequest request) {
    String appKey = request.getParameter("appKey");
    if (Strings.isNullOrEmpty(appKey)) {
      return;
    }
    BeanFactory factory = WebApplicationContextUtils
            .getRequiredWebApplicationContext(request.getServletContext());
    ClientFinder clientFinder = factory.getBean(ClientFinder.class);
    ClientInfo clientInfo =  clientFinder.get(appKey);
    if (clientInfo == null) {
      throw SystemException.create(DefaultErrorCode.PERMISSION_DENIED)
              .setDetails("undefined appkey");
    }
    checkSign(request, clientInfo);
    ClientHolder.set(clientInfo);
  }

  private void checkSign(
          HttpServletRequest request, ClientInfo clientInfo) {
    String secret = clientInfo.getAppSecret();
    String signMethod = request.getParameter("signMethod");
    if (Strings.isNullOrEmpty(signMethod)) {
      throw SystemException.create(DefaultErrorCode.INVALID_REQ)
              .setDetails("signMethod required");
    }
   List<String> optionMethods = new ArrayList<>();
    optionMethods.add("HMACSHA256");
    optionMethods.add("HMACSHA512");
    optionMethods.add("HMACMD5");
    optionMethods.add("MD5");
    if (!optionMethods.contains(signMethod)) {
      throw SystemException.create(DefaultErrorCode.INVALID_REQ)
              .setDetails("signMethod only support HMACSHA256 | HMACSHA512 | HMACMD5 | MD5");
    }
  String clientSignValue = request.getParameter("sign");
    if (Strings.isNullOrEmpty(clientSignValue)) {
      throw SystemException.create(DefaultErrorCode.INVALID_REQ)
              .setDetails("sign required");
    }
    String serverSignValue = signTopRequest(request, secret, signMethod);
    if (!clientSignValue.equalsIgnoreCase(serverSignValue)) {
      throw  SystemException.create(DefaultErrorCode.INVALID_REQ)
              .set("details", "Incorrect sign");
    }
  }

  private String signTopRequest(HttpServletRequest request, String secret, String signMethod) {
    String queryString = baseString(request);
    String sign = null;
    try {
      if (EncryptUtils.HMACMD5.equalsIgnoreCase(signMethod)) {
        sign = EncryptUtils.encryptHmacMd5(queryString, secret);
      } else if (EncryptUtils.HMACSHA256.equalsIgnoreCase(signMethod)) {
        sign = EncryptUtils.encryptHmacSha256(queryString, secret);
      } else if (EncryptUtils.HMACSHA512.equalsIgnoreCase(signMethod)) {
        sign = EncryptUtils.encryptHmacSha512(queryString, secret);
      } else if (EncryptUtils.MD5.equalsIgnoreCase(signMethod)) {
        sign = EncryptUtils.encryptMD5(secret + queryString + secret);
      }
    } catch (IOException e) {

    }
    return sign;
  }

  private String baseString(HttpServletRequest request) {
    // 第一步：检查参数是否已经排序
    String[] keys = request.getParameterMap().keySet().toArray(new String[request.getParameterMap().size()]);
    Arrays.sort(keys);

    // 第二步：把所有参数名和参数值串在一起
    List<String> query = new ArrayList<>(keys.length);
    for (String key : keys) {
      String value = request.getParameter(key);
      if (!Strings.isNullOrEmpty(value)) {
        query.add(key + "=" + value);
      }
    }
    return Joiner.on("&").join(query);
  }
}