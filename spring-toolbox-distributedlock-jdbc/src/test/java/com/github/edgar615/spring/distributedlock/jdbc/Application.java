package com.github.edgar615.spring.distributedlock.jdbc;

import com.github.edgar615.spring.distributedlock.DistributedLockProvider;
import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Configuration
@EnableTransactionManagement
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public DistributedLockProvider distributedLockProvider(DataSource dataSource) {
    return new SimpleJdbcDistributedLockProvider(new JdbcTemplate(dataSource));
  }

}
