package com.github.edgar615.util.spring.operatelog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edgar615.util.spring.auth.Principal;
import com.github.edgar615.util.spring.auth.PrincipalHolder;
import com.github.edgar615.util.spring.jwt.JwtHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Aspect
public class OperateLogAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(OperateLogAspect.class);

  @Autowired
  private ApplicationEventPublisher publisher;

  @Pointcut("@annotation(com.github.edgar615.util.spring.operatelog.UserOperate)")
  public void controllerAspect() {}

  @AfterReturning(value = "controllerAspect() && @annotation(annotation) ", argNames = "annotation")
  public void interceptorUserOperate(JoinPoint joinPoint, UserOperate annotation) {
    try {
      log(joinPoint, annotation);
    } catch (Exception e) {
      LOGGER.warn("log error", e);
    }
  }

  private void log(JoinPoint joinPoint, UserOperate annotation) throws JsonProcessingException {
    HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    OperateLog sysLog = new OperateLog();
    sysLog.setLogTime((int) Instant.now().getEpochSecond());
    sysLog.setType(1);
    sysLog.setOperatorAction(annotation.action());
    sysLog.setBusinessMoudle(annotation.module());
    sysLog.setRequestUrl(request.getRequestURI());
    sysLog.setClientIp(getIpAddr(request));
    // 从请求中获取参数列表
//    Enumeration<String> parameterNames = request.getParameterNames();
//    Map<String, String> requestParams = new HashMap<>();
//    while (parameterNames.hasMoreElements()) {
//      String name = parameterNames.nextElement();
//      String value = request.getParameter(name);
//      requestParams.put(name, value);
//    }
    sysLog.setClassName(joinPoint.getTarget().getClass().getName());
    String methodName = joinPoint.getSignature().getName();
    sysLog.setMethodName(methodName);
    Map<String, Object> methodParams = new HashMap<>();
    int parameterCount = joinPoint.getArgs() == null ? 0 : joinPoint.getArgs().length;
    if (parameterCount != 0) {
      Method[] methods = ReflectionUtils.getAllDeclaredMethods(joinPoint.getSignature()
                                                                       .getDeclaringType());
      for (Method method : methods) {
        if (method.getName().equals(methodName)
            && method.getParameterCount() == parameterCount) {
          LocalVariableTableParameterNameDiscoverer u =
                  new LocalVariableTableParameterNameDiscoverer();
          String[] params = u.getParameterNames(method);
          for (int i = 0; i < params.length; i++) {
            methodParams.put(params[i], joinPoint.getArgs()[i]);
          }
//          Class[] classes = method.getParameterTypes();
//          Annotation[][] annotations = method.getParameterAnnotations();
//          for (Annotation[] annotations1: annotations) {
//            for (Annotation annotation1: annotations1) {
//              System.out.println(annotation1.annotationType());
//            }
//          }
        }
      }
      ObjectMapper mapper = new ObjectMapper();
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      sysLog.setRequestParam(mapper.writeValueAsString(methodParams));
    }

    Principal principal = PrincipalHolder.get();
    if (principal != null) {
      sysLog.setOperatorId(principal.getUserId().intValue());
      sysLog.setOperator(principal.getUsername());
    }

    publisher.publishEvent(new UserOperateEvent(sysLog));

  }

  /**
   * 获取IP地址
   *
   * @param request
   * @return
   */
  private String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

}
