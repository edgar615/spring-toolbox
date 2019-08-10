package com.github.edgar615.util.spring.appkey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

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
//    对于单体应用，写死
    clientInfo.setAppId(clientFixProperties.getAppId());
    clientInfo.setAppKey(clientFixProperties.getAppKey());
    clientInfo.setAppSecret(clientFixProperties.getAppSecret());
    clientInfo.setAppName(clientFixProperties.getAppName());
    if (clientFixProperties != null) {
      clientFixProperties.getExt().forEach((k, v) -> clientInfo.addExt(k, v));
    }
    ClientHolder.set(clientInfo);
    return super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, @Nullable Exception ex) throws Exception {
    ClientHolder.clear();
    super.afterCompletion(request, response, handler, ex);
  }
}
