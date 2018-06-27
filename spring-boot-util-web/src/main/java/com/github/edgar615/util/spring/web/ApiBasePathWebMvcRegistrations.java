package com.github.edgar615.util.spring.web;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * Created by Edgar on 2018/6/27.
 *
 * @author Edgar  Date 2018/6/27
 */
public class ApiBasePathWebMvcRegistrations implements WebMvcRegistrations {

  private final String apiBasePath;

  public ApiBasePathWebMvcRegistrations(String apiBasePath) {
    if (apiBasePath == null) {
      this.apiBasePath = "/api";
    } else {
      this.apiBasePath = apiBasePath;
    }
  }


  @Override
  public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
    return new RequestMappingHandlerMapping() {

      @Override
      protected void registerHandlerMethod(Object handler, Method method,
                                           RequestMappingInfo mapping) {
        Class<?> beanType = method.getDeclaringClass();
        if (AnnotationUtils.findAnnotation(beanType, RestController.class) != null) {
          PatternsRequestCondition
                  apiPattern = new PatternsRequestCondition(apiBasePath)
                  .combine(mapping.getPatternsCondition());

          mapping = new RequestMappingInfo(mapping.getName(), apiPattern,
                                           mapping.getMethodsCondition(),
                                           mapping.getParamsCondition(),
                                           mapping.getHeadersCondition(),
                                           mapping.getConsumesCondition(),
                                           mapping.getProducesCondition(),
                                           mapping.getCustomCondition());
        }

        super.registerHandlerMethod(handler, method, mapping);
      }
    };
  }
}
