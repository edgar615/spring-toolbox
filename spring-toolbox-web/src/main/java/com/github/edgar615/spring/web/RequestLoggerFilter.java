package com.github.edgar615.spring.web;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Doogies very cool HTTP request logging
 * <p>
 * There is also {@link org.springframework.web.filter.CommonsRequestLoggingFilter}  but it
 * cannot log request method
 * And it cannot easily be extended.
 * <p>
 * https://mdeinum.wordpress.com/2015/07/01/spring-framework-hidden-gems/
 * http://stackoverflow.com/questions/8933054/how-to-read-and-copy-the-http-servlet-response
 * -output-stream-content-for-logging
 */
public class RequestLoggerFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggerFilter.class);

//  private boolean includeResponsePayload = true;

  private final List<String> ignorePrefixes = new ArrayList<>();

  private int maxPayloadLength = 1000;

  private boolean logReqBody = true;

  /**
   * Log each request and respponse with full Request URI, content payload and duration of the
   * request in ms.
   *
   * @param request     the request
   * @param response    the response
   * @param filterChain chain of filters
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    String url = request.getServletPath();
    for (String prefix : ignorePrefixes) {
      if (url.startsWith(prefix)) {
        filterChain.doFilter(request, response);
        return;
      }
    }

//    //从请求头中取出X-Request-Id，用于全局跟踪ID，如果未找到，自动生成一个新的跟踪ID
    String traceId = request.getHeader("X-Request-Id");
    if (Strings.isNullOrEmpty(traceId)) {
      traceId = UUID.randomUUID().toString();
    }
    MDC.put("x-request-id", traceId);

    // ========= Log request and response payload ("body") ========
    // We CANNOT simply read the request payload here, because then the InputStream would be
    // consumed and cannot be read again by the actual processing/server.
    //    String reqBody = DoogiesUtil._stream2String(request.getInputStream());   // THIS WOULD
    // NOT WORK!
    // So we need to apply some stronger magic here :-)
    ResettableStreamRequestWrapper wrappedRequest
            = new ResettableStreamRequestWrapper(request);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

    // I can only log the request's body AFTER the request has been made and
    // ContentCachingRequestWrapper did its work.
    String body = wrappedRequest.getRequestBody();
//     [跟踪ID] [SR] [类型] [接口] [入参]...[入参] [调用方]
    LOGGER.info("[{}] [SR] [HTTP] [{} {}] [{}] [{}] [{}] [{}]", traceId, request.getMethod(), request.getServletPath(),
            headerString(request), paramString(request),
            !logReqBody || Strings.isNullOrEmpty(body) ? "no body" : body,
            getClientIp(request));
    long startTime = System.currentTimeMillis();
    filterChain.doFilter(wrappedRequest,
            wrappedResponse);     // ======== This performs the actual request!
    wrappedResponse.addHeader("X-Request-Id", traceId);

    //[跟踪ID] [SS] [类型] [结果] ... [结果] [耗时]
    LOGGER.info("[{}] [SS] [HTTP] [{}] [{}] [{}bytes] [{}ms]", traceId,
            response.getStatus(),
            respHeaderString(response),
            wrappedResponse.getContentAsByteArray().length,
            System.currentTimeMillis() - startTime);

    wrappedResponse
            .copyBodyToResponse();
    MDC.remove("x-request-id");
    // IMPORTANT: copy content of response back into original response

  }

  public void addIgnorePrefix(String prefix) {
    ignorePrefixes.add(prefix);
  }

  public void setLogReqBody(boolean logReqBody) {
    this.logReqBody = logReqBody;
  }

  @Deprecated
  private String getBody(ContentCachingRequestWrapper request) {
    // wrap request to make sure we can read the body of the request (otherwise it will be consumed by the actual
    // request handler)
    ContentCachingRequestWrapper wrapper = WebUtils
            .getNativeRequest(request, ContentCachingRequestWrapper.class);
    if (wrapper != null) {
      byte[] buf = wrapper.getContentAsByteArray();
      if (buf.length > 0) {
        String payload;
        try {
          payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
        } catch (UnsupportedEncodingException ex) {
          payload = "unknown";
        }

        return payload;
      }
    }
    return "no body";
  }

  @Deprecated
  private String getContentAsString(byte[] buf, int maxLength, String charsetName) {
    if (buf == null || buf.length == 0) return "";
    int length = Math.min(buf.length, this.maxPayloadLength);
    try {
      return new String(buf, 0, length, charsetName);
    } catch (UnsupportedEncodingException ex) {
      return "Unsupported Encoding";
    }
  }

  private String headerString(HttpServletRequest request) {
    StringBuilder sb = new StringBuilder();
    Enumeration<String> headers = request.getHeaderNames();
    while (headers.hasMoreElements()) {
      String name = headers.nextElement();
      Enumeration<String> value = request.getHeaders(name);
      sb.append(name).append(":")
              .append(Joiner.on(",").join(Iterators.forEnumeration(value)))
              .append(";");
    }
    if (sb.length() == 0) {
      return "no header";
    }
    return sb.toString();
  }

  private String paramString(HttpServletRequest request) {
    StringBuilder sb = new StringBuilder();
    Enumeration<String> names = request.getParameterNames();
    while (names.hasMoreElements()) {
      String name = names.nextElement();
      String[] values = request.getParameterValues(name);
      sb.append(name).append(":")
              .append(Joiner.on(",").join(Iterators.forArray(values)))
              .append(";");
    }
    if (sb.length() == 0) {
      return "no param";
    }
    return sb.toString();
  }

  private String respHeaderString(HttpServletResponse response) {
    StringBuilder sb = new StringBuilder();
    Collection<String> headers = response.getHeaderNames();
    for (String name : headers) {
      sb.append(name).append(":")
              .append(Joiner.on(",").join(response.getHeaders(name)))
              .append(";");
    }
    if (sb.length() == 0) {
      return "no header";
    }
    return sb.toString();
  }

  private String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (!Strings.isNullOrEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      //多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(",");
      if (index != -1) {
        return ip.substring(0, index);
      } else {
        return ip;
      }
    }
    ip = request.getHeader("X-Real-IP");
    if (!Strings.isNullOrEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      return ip;
    }
    return request.getRemoteHost();
  }

}
