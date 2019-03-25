package com.github.edgar615.util.mybatis;

import com.github.edgar615.util.spring.jdbc.JdbcCacheProperties;
import com.github.edgar615.util.spring.jdbc.JdbcCacheProperties.JdbcCacheConfigSpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;

public class MybatisJdbcCacheResolver extends SimpleCacheResolver {

  private static final String DEFAULT_NAME = "Default";

  private final JdbcCacheProperties jdbcCacheProperties;


  protected MybatisJdbcCacheResolver(CacheManager cacheManager,
      JdbcCacheProperties jdbcCacheProperties) {
    super(cacheManager);
    this.jdbcCacheProperties = jdbcCacheProperties;
  }

  @Override
  protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
    return determineCacheName(context);
  }

  private String determinePrefixCacheName(String prefixCacheName) {
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
    List<String> resolvedCacheNames = new ArrayList<>();
    for (String unresolvedCacheName : unresolvedCacheNames) {
      if (!unresolvedCacheName.endsWith("JdbcCache")) {
        resolvedCacheNames.add(unresolvedCacheName);
      } else {
        String prefixKey = unresolvedCacheName.substring(0, unresolvedCacheName.length() - "JdbcCache".length());
        prefixKey = determinePrefixCacheName(prefixKey);
        resolvedCacheNames.add(prefixKey + "JdbcCache");
      }
    }
    return resolvedCacheNames;
  }

}
