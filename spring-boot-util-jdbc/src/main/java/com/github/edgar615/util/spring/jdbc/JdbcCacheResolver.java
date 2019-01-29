package com.github.edgar615.util.spring.jdbc;

import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;
import com.github.edgar615.util.spring.jdbc.JdbcCacheProperties.JdbcCacheConfigSpec;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;

public class JdbcCacheResolver extends SimpleCacheResolver {

  private static final String DEFAULT_NAME = "Default";

  private final JdbcCacheProperties jdbcCacheProperties;


  protected JdbcCacheResolver(CacheManager cacheManager,
      JdbcCacheProperties jdbcCacheProperties) {
    super(cacheManager);
    this.jdbcCacheProperties = jdbcCacheProperties;
  }

  @Override
  protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
    return determineCacheName(context);
  }

  private String determinePrefixCacheName(CacheOperationInvocationContext<?> context) {
    String prefixCacheName = tableName(context);
    if (jdbcCacheProperties.getConfig().getCustomSpec() == null) {
      return prefixCacheName;
    }
    JdbcCacheConfigSpec spec = jdbcCacheProperties.getConfig().getCustomSpec().get(prefixCacheName);
    if (spec == null) {
      if (matchDefault(prefixCacheName)) {
        return DEFAULT_NAME;
      }
      return prefixCacheName;
    }
    return spec.getCacheNamePrefix();
  }

  private boolean matchDefault(String tableName) {
    if (jdbcCacheProperties.getDefaultCacheTables() == null) {
      return false;
    }
    return jdbcCacheProperties.getDefaultCacheTables().contains(tableName);
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

  public String tableName(CacheOperationInvocationContext<?> context) {
    Object obj = context.getArgs()[0];
    if (obj instanceof Class) {
      return ((Class) obj).getSimpleName();
    }
    if (obj instanceof Persistent) {
      return obj.getClass().getSimpleName();
    }
    if (obj instanceof List) {
      List list = (List) obj;
      if (list.isEmpty() || list == null) {
        throw SystemException.create(DefaultErrorCode.INVALID_ARGS);
      }
      return list.get(0).getClass().getSimpleName();
    }
    throw new UnsupportedOperationException("JdbcCacheResolver");
  }

}
