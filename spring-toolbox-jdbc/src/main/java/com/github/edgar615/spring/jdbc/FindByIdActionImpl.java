package com.github.edgar615.spring.jdbc;

import com.github.edgar615.util.db.Persistent;
import javax.sql.DataSource;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig(cacheResolver = "jdbcCacheResolver")
public class FindByIdActionImpl implements FindByIdAction {

  private final JdbcOperation jdbcOperation;

  public FindByIdActionImpl(DataSource dataSource) {
    this.jdbcOperation = new JdbcOperationImpl(dataSource);
  }

  @Override
  @Cacheable(cacheNames = "JdbcCache", key = "#p0.getSimpleName() + ':' + #p1")
  public <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id) {
    return jdbcOperation.findById(elementType, id);
  }
}
