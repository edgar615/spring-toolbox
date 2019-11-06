package com.github.edgar615.spring.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edgar615.util.exception.CustomErrorCode;
import com.github.edgar615.util.exception.SystemException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

import static feign.FeignException.errorStatus;

@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String methodKey, Response response) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      Map<String, Object> map = mapper.readValue(response.body().asInputStream(), Map.class);
      int code = (int) map.getOrDefault("code", 999);
      String message = (String) map.getOrDefault("message", "no message");
      CustomErrorCode errorCode = CustomErrorCode.create(code, message);
      map.remove("code");
      map.remove("message");
      return SystemException.create(errorCode).setAll(map);
    } catch (Exception e) {
      return errorStatus(methodKey, response);
    }
  }
}
