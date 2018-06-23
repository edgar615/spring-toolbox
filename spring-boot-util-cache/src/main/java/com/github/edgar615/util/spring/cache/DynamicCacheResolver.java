package com.github.edgar615.util.spring.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DynamicCacheResolver extends SimpleCacheResolver {

  public DynamicCacheResolver(CacheManager cacheManager) {
    super(cacheManager);
  }

  @Override
  protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
    List<String> unresolvedCacheNames = new ArrayList<>(super.getCacheNames(context));
    DynamicCacheName dynamicCacheName = context.getMethod().getAnnotation(DynamicCacheName.class);
    if (dynamicCacheName == null) {
      return unresolvedCacheNames;
    }
    if (dynamicCacheName.value().length == 0) {
      return unresolvedCacheNames;
    }
    ExpressionParser parser = new SpelExpressionParser();
    StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
    evaluationContext.setVariable("methodName", context.getMethod().getName());
    for (int i = 0; i < context.getArgs().length; i ++) {
      evaluationContext.setVariable("p" + i, context.getArgs()[i]);
    }
    List<String> resolvedCacheNames = new ArrayList<>();
    for (int i = 0; i < unresolvedCacheNames.size();  i ++) {
      String suffixKey = parser.parseExpression(dynamicCacheName.value()[i]).getValue
              (evaluationContext, String.class);
      resolvedCacheNames.add(unresolvedCacheNames.get(i) + "-" + suffixKey);
    }
    return resolvedCacheNames;
  }

}