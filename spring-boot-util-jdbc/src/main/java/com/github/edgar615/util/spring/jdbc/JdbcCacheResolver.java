package com.github.edgar615.util.spring.jdbc;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class JdbcCacheResolver extends SimpleCacheResolver {

  private final JdbcCacheProperties jdbcCacheProperties;

  public JdbcCacheResolver(CacheManager cacheManager, JdbcCacheProperties jdbcCacheProperties) {
    super(cacheManager);
    this.jdbcCacheProperties = jdbcCacheProperties;
  }

  @Override
  protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
      return determineCacheName(context);
  }

  private String determinePrefixCacheName(CacheOperationInvocationContext<?> context) {
    JdbcCache jdbcCache = context.getMethod().getAnnotation(JdbcCache.class);
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
    if (jdbcCacheProperties.getConfig() != null) {
      Set<String> cacheNames = jdbcCacheProperties.getConfig().keySet();
      for (String cacheName : cacheNames) {
        Set<String> tables = jdbcCacheProperties.getConfig().get(cacheName)
            .keySet();
        if (tables.contains(prefixCacheName)) {
          return cacheName;
        }
      }
    }
    return prefixCacheName;
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

}
