package com.github.edgar615.spring.web.log;

import com.google.common.base.Strings;
import java.util.Map;

public class ReuqestContext {

  private final String CLIENT_IP_DIRECTIVE = "c";

  private final String USER_DIRECTIVE = "u";

  private final String REQUEST_TIME_DIRECTIVE = "t";

  private final String HTTP_VERSION_DIRECTIVE = "v";

  private final String METHOD_DIRECTIVE = "m";

  private final String URI_DIRECTIVE = "u";

  private final String QUERY_DIRECTIVE = "q";

  private final String HEADER_DIRECTIVE = "H";

  private final String BODY_DIRECTIVE = "B";

  private final String BODY_LEN_DIRECTIVE = "b";

  private static final String EMPTY = "-";

  /**
   * 客户端IP
   */
  private String clientIp;

  /**
   * 用户
   */
  private String user;

  /**
   * 请求时间
   */
  private String requestTime;

  /**
   * 请求方法
   */
  private String method;

  /**
   * 请求地址
   */
  private String uri;

  /**
   * 查询字符串
   */
  private Map<String, Object> query;

  /**
   * HTTP版本
   */
  private String httpVersion;

  private String body;

  private Map<String, Object> header;

  /**
   * 根据指令转换为值，这里没有做太复杂的模式，直接用if/else实现
   * @param percentDirective
   * @return
   */
  public String get(String percentDirective) {
    if (CLIENT_IP_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(clientIp);
    }
    if (USER_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(user);
    }
    if (REQUEST_TIME_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(requestTime);
    }
    if (HTTP_VERSION_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(httpVersion);
    }
    if (METHOD_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(method);
    }
    if (URI_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(uri);
    }
    if (QUERY_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(uri);
    }
    if (HEADER_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(uri);
    }
    if (BODY_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(uri);
    }
    if (BODY_LEN_DIRECTIVE.equals(percentDirective)) {
      return getOrDefault(uri);
    }

    return EMPTY;
  }

  private String getOrDefault(String value) {
    if (Strings.isNullOrEmpty(value)) {
      return EMPTY;
    }
    return value;
  }
}
