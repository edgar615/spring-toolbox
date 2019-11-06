package com.github.edgar615.spring.distributedlock.jdbc;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LockServiceImpl implements LockService {

  private final JdbcTemplate jdbcTemplate;

  public LockServiceImpl(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }


  @Override
  @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
  public int count() {
    List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from distributed_lock");
    return result.size();
  }

  @Override
  @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
  public void clear() {
    jdbcTemplate.update("delete form distributed_lock");
  }
}
