package com.github.edgar615.spring.jdbc;

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

  static JdbcTicket create(String ticketTable, String stub, int maxSize, DataSource dataSource,
                           ExecutorService executorService) {
    return new JdbcTicketImpl(ticketTable, stub, maxSize, dataSource, executorService);
  }
}
