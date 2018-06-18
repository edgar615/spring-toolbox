package com.github.edgar615.util.spring.appkey;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ou
 * 权限拦截。
 */
public class FixClientInterceptor extends HandlerInterceptorAdapter {

  private final ClientFixProperties clientFixProperties;

  public FixClientInterceptor(ClientFixProperties clientFixProperties) {
    this.clientFixProperties = clientFixProperties;
  }

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler) throws Exception {
    ClientInfo clientInfo = new ClientInfo();
//    对于单机版应用，不需要关注appKey和appSecret，只需要记录平台分配的companyCode，所以我们这里使用了一个随机的字符串
    clientInfo.setAppKey(clientFixProperties.getAppKey());
    clientInfo.setAppSecret(clientFixProperties.getAppSecret());
    clientInfo.setCompanyCode(clientFixProperties.getCompanyCode());
    clientInfo.setName(clientFixProperties.getName());
    ClientHolder.set(clientInfo);
    return super.preHandle(request, response, handler);
  }
}