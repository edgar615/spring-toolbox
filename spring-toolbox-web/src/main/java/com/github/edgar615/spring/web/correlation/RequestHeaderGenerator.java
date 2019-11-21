package com.github.edgar615.spring.web.correlation;

import javax.servlet.http.HttpServletRequest;

/**
 * requestId的UUID生成器
 */
public class RequestHeaderGenerator implements CorrelationIdGenerator {

    /**
     * 使用UUID生成requestId.
     * @param request http请求
     * @return random uuid
     */
    @Override
    public String generate(HttpServletRequest request) {
       return request.getHeader(RequestCorrelationConsts.HEADER_NAME);
    }
}
