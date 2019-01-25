package com.github.edgar615.util.spring.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;

public class JdbcCacheResolver extends SimpleCacheResolver {

//  private final PropertyResolver propertyResolver;

  public JdbcCacheResolver(CacheManager cacheManager) {
    super(cacheManager);
  }

  @Override
  protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
    List<String> unresolvedCacheNames = new ArrayList<>(super.getCacheNames(context));
    JdbcCache jdbcCache = context.getMethod().getAnnotation(JdbcCache.class);
    if (jdbcCache == null) {
      return unresolvedCacheNames;
    }
//    getCacheManager().getCache()
    return null;
//    if (jdbcCache.value().length == 0) {
//      return unresolvedCacheNames;
//    }
//    ExpressionParser parser = new SpelExpressionParser();
//    StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
//    evaluationContext.setVariable("methodName", context.getMethod().getName());
//    for (int i = 0; i < context.getArgs().length; i ++) {
//      evaluationContext.setVariable("p" + i, context.getArgs()[i]);
//    }
//    List<String> resolvedCacheNames = new ArrayList<>();
//    for (int i = 0; i < unresolvedCacheNames.size();  i ++) {
//      String suffixKey = parser.parseExpression(jdbcCache.value()[i]).getValue
//              (evaluationContext, String.class);
//      resolvedCacheNames.add(unresolvedCacheNames.get(i) + "-" + suffixKey);
//    }
//    return resolvedCacheNames;
  }

//  private Collection<Cache> getCaches(CacheManager cacheManager, CacheOperationInvocationContext<?> context) {
//    return context.getOperation().getCacheNames().stream()
//        .map(cacheName -> cacheManager.getCache(cacheName))
//        .filter(cache -> cache != null)
//        .collect(Collectors.toList());
//  }

}
