package com.github.edgar615.spring.binlog;

/**
 * Created by Edgar on 2018/5/22.
 *
 * @author Edgar  Date 2018/5/22
 */
public interface BinlogStream {
  void close();

  void start();

  static BinlogStream create(BinlogProperties binlogProperties,
                             DataSourceProperties dataSourceProperties) {
    return new BinlogStreamImpl(binlogProperties, dataSourceProperties);
  }
}
