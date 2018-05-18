package com.github.edgar615.util.spring.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import javax.sql.DataSource;

/**
 * CREATE TABLE `Tickets64` (
 * `id` bigint(20) unsigned NOT NULL auto_increment,
 * `stub` varchar(32) NOT NULL default '',
 * PRIMARY KEY  (`id`),
 * UNIQUE KEY `stub` (`stub`)
 * ) ENGINE=MyISAM
 * <p>
 * <p>
 * REPLACE INTO Tickets64 (stub) VALUES ('a');
 * SELECT LAST_INSERT_ID();
 *
 * @author Edgar  Date 2018/5/18
 */
class JdbcTicketImpl implements JdbcTicket {

  private final String stub;

  private final int maxSize;

  private final DataSource dataSource;

  private final LinkedList<Long> ticketQueue = new LinkedList<>();

  private final ExecutorService executorService;

  private volatile boolean jobRunning = false;

  JdbcTicketImpl(String stub, int maxSize, DataSource dataSource,
                 ExecutorService executorService) {
    this.stub = stub;
    this.maxSize = maxSize;
    this.dataSource = dataSource;
    this.executorService = executorService;
  }

  @Override
  public synchronized long ticket() {
    Long ticket = ticketQueue.poll();
    if (ticket == null) {
      ticket = reqTicket();
    }
    if (ticketQueue.size() <= maxSize) {
      batchReq();
    }
    return ticket;
  }

  public void batchReq() {
    if (jobRunning) {
      return;
    }
    jobRunning = true;
    while (ticketQueue.size() < maxSize) {
      executorService.submit(() -> {
        ticketQueue.add(reqTicket());
      });
    }
    jobRunning = false;
  }

  private long reqTicket() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    long id = jdbcTemplate.execute((StatementCallback<Long>) stmt -> {
      stmt.execute("replace into ticket64 (stub) values ('" + stub + "')");
      ResultSet resultSet = stmt.executeQuery("select last_insert_id() as id");
      long result = -1;
      if (resultSet.next()) {
        result = resultSet.getLong("id");
      }
      return result;
    });
    return id;
  }
}
