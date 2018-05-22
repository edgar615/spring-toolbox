package com.github.edgar615.util.spring.cache.binlog;

import com.github.edgar615.util.spring.cache.StartCacheManager;

/**
 * Created by Edgar on 2018/5/22.
 *
 * @author Edgar  Date 2018/5/22
 */
public interface BinlogStream {
  void close();

  void start();

  static BinlogStream create(BinlogProperties binlogProperties,
                             DataSourceProperties dataSourceProperties,
                             StartCacheManager cacheManager) {
    return new BinlogStreamImpl(binlogProperties, dataSourceProperties, cacheManager);
  }
}
