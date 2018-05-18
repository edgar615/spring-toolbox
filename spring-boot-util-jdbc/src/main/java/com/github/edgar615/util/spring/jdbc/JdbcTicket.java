package com.github.edgar615.util.spring.jdbc;

import java.util.concurrent.ExecutorService;
import javax.sql.DataSource;

/**
 * Created by Edgar on 2018/5/18.
 *
 * @author Edgar  Date 2018/5/18
 */
public interface JdbcTicket {

  /**
   * 获取一个ticket
   *
   * @return
   */
  long ticket();

  static JdbcTicket create(String stub, int maxSize, DataSource dataSource,
                           ExecutorService executorService) {
    return new JdbcTicketImpl(stub, maxSize, dataSource, executorService);
  }
}
