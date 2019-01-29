package com.github.edgar615.util.spring.jdbc;

import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;
import com.github.edgar615.util.spring.jdbc.JdbcCacheProperties.JdbcCacheConfigSpec;
import com.google.common.base.Strings;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Deprecated
public class DynamicJdbcCacheResolver extends SimpleCacheResolver {

  private final JdbcCacheProperties jdbcCacheProperties;

  public DynamicJdbcCacheResolver(CacheManager cacheManager, JdbcCacheProperties jdbcCacheProperties) {
    super(cacheManager);
    this.jdbcCacheProperties = jdbcCacheProperties;
  }

  @Override
  protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
      return determineCacheName(context);
  }

  private String determinePrefixCacheName(CacheOperationInvocationContext<?> context) {
    JdbcCache jdbcCache = getAnnotation(context, JdbcCache.class);
    if (jdbcCache == null || Strings.isNullOrEmpty(jdbcCache.value())) {
      return null;
    }
    ExpressionParser parser = new SpelExpressionParser();
    StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
    evaluationContext.setVariable("methodName", context.getMethod().getName());
    for (int i = 0; i < context.getArgs().length; i++) {
      evaluationContext.setVariable("p" + i, context.getArgs()[i]);
    }
    String prefixCacheName = parser.parseExpression(jdbcCache.value()).getValue
        (evaluationContext, String.class);
    if (jdbcCacheProperties.getConfig().getCustomSpec() == null) {
      return prefixCacheName;
    }
    JdbcCacheConfigSpec spec = jdbcCacheProperties.getConfig().getCustomSpec().get(prefixCacheName);
    if (spec == null) {
      return prefixCacheName;
    }
    return spec.getCacheNamePrefix();
  }

  private Collection<String> determineCacheName(CacheOperationInvocationContext<?> context) {
    List<String> unresolvedCacheNames = new ArrayList<>(super.getCacheNames(context));
    String prefixKey = determinePrefixCacheName(context);
    if (Strings.isNullOrEmpty(prefixKey)) {
      return unresolvedCacheNames;
    }
    List<String> resolvedCacheNames = new ArrayList<>();
    for (int i = 0; i < unresolvedCacheNames.size(); i++) {
      resolvedCacheNames.add(prefixKey + unresolvedCacheNames.get(i));
    }
    return resolvedCacheNames;
  }

  private <T extends Annotation> T getAnnotation(CacheOperationInvocationContext<?> context, Class<T> clazz) {
    try {
      // due to some cache proxy behaviour we can get method of superinterface instead of annotated method from target class
      // but sometime annotation has been appear on interface therefore we need check both cases
      Method proxiedMethod = context.getMethod();
      Class<?> targetClazz = context.getTarget().getClass();
      T annotation = null;
      if(!targetClazz.equals(proxiedMethod.getDeclaringClass())) {
        Method origMethod = targetClazz.getMethod(proxiedMethod.getName(), proxiedMethod.getParameterTypes());
        annotation = origMethod.getAnnotation(clazz);
      }
      if(annotation == null) {
        annotation = proxiedMethod.getAnnotation(clazz);
      }
      return annotation;
    } catch (NoSuchMethodException e) {
      throw SystemException.wrap(DefaultErrorCode.INVALID_ARGS, e);
    }
  }

}
