package com.github.edgar615.util.spring.appkey;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import com.github.edgar615.util.base.EncryptUtils;
import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;
import com.github.edgar615.util.spring.web.ResettableStreamRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 从请求头中解析对应的Client.
 */
public class ClientInfoInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientInfoInterceptor.class);

  private final ClientFinder clientFinder;

  public ClientInfoInterceptor(ClientFinder clientFinder) {this.clientFinder = clientFinder;}

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

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, @Nullable Exception ex) throws Exception {
    ClientHolder.clear();
    super.afterCompletion(request, response, handler, ex);
  }

  private void findAndCheckClient(HttpServletRequest request) {
    String appKey = request.getParameter("appKey");
    if (Strings.isNullOrEmpty(appKey)) {
      throw SystemException.create(DefaultErrorCode.MISSING_ARGS)
              .setDetails("appKey");
    }
    ClientInfo clientInfo = clientFinder.get(appKey);
    if (clientInfo == null) {
      throw SystemException.create(ClientError.NON_EXISTED_APP_KEY);
    }
    checkSign(request, clientInfo);
    ClientHolder.set(clientInfo);
  }

  private void checkSign(
          HttpServletRequest request, ClientInfo clientInfo) {
    String secret = clientInfo.getAppSecret();
    String signMethod = request.getParameter("signMethod");
    if (Strings.isNullOrEmpty(signMethod)) {
      throw SystemException.create(DefaultErrorCode.MISSING_ARGS)
              .setDetails("signMethod");
    }
    List<String> optionMethods = new ArrayList<>();
    optionMethods.add("HMACSHA256");
    optionMethods.add("HMACSHA512");
    optionMethods.add("HMACMD5");
    optionMethods.add("MD5");
    if (!optionMethods.contains(signMethod)) {
      throw SystemException.create(ClientError.UNSUPPORTED_SING_METHOD)
              .setDetails("signMethod only support HMACSHA256 | HMACSHA512 | HMACMD5 | MD5");
    }
    String clientSignValue = request.getParameter("sign");
    if (Strings.isNullOrEmpty(clientSignValue)) {
      throw SystemException.create(DefaultErrorCode.MISSING_ARGS)
              .setDetails("sign");
    }
    String serverSignValue = signTopRequest(request, secret, signMethod);
    if (!clientSignValue.equalsIgnoreCase(serverSignValue)) {
      throw SystemException.create(ClientError.INCORRECT_SIGN);
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

  private Map<String, String> baseMap(HttpServletRequest request) {
    Map<String, String> params = new HashMap<>();
    for (String key : request.getParameterMap().keySet()) {
      String value = request.getParameter(key);
      if (!key.equalsIgnoreCase("sign") && !Strings.isNullOrEmpty(value)) {
        params.put(key, value);
      }
    }
    if (request.getMethod().equalsIgnoreCase("POST")
        || request.getMethod().equalsIgnoreCase("PUT")) {
      if (request instanceof ResettableStreamRequestWrapper) {
        ResettableStreamRequestWrapper requestWrapper = (ResettableStreamRequestWrapper) request;
        params.put("body", requestWrapper.getRequestBody());
      }
    }
    return params;
  }

  private String baseString(HttpServletRequest request) {
    Map<String, String> params = baseMap(request);
    // 第一步：检查参数是否已经排序
    String[] keys = params.keySet().toArray(new String[params.size()]);

    Arrays.sort(keys);

    // 第二步：把所有参数名和参数值串在一起
    List<String> query = new ArrayList<>(keys.length);
    for (String key : keys) {
      query.add(key + "=" + params.get(key));
    }
    return Joiner.on("&").join(query);
  }
}