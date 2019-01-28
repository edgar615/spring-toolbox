package com.github.edgar615.util.spring.jdbc;

import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.db.SQLBindings;
import com.github.edgar615.util.db.SqlBuilder;
import com.google.common.collect.Lists;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@CacheConfig(cacheResolver = "jdbcCacheResolver")
public class FindByIdActionImpl implements FindByIdAction {

  private final DataSource dataSource;

  public FindByIdActionImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  @Cacheable(cacheNames = "JdbcCache", key = "#p1")
  @JdbcCache("#p0.getSimpleName()")
  public <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id) {
    SQLBindings sqlBindings = SqlBuilder.findById(elementType, id, Lists.newArrayList());
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    List<T> result =
        jdbcTemplate.query(sqlBindings.sql(), sqlBindings.bindings().toArray(),
            BeanPropertyRowMapper.newInstance(elementType));
    if (result.isEmpty()) {
      return null;
    }
    return result.get(0);
  }
}
