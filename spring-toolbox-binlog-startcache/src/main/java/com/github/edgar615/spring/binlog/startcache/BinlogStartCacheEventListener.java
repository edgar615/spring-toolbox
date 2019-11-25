package com.github.edgar615.spring.binlog.startcache;

import com.github.edgar615.spring.binlog.DbChangedData;
import com.github.edgar615.spring.binlog.DbDataChangedEvent;
import com.github.edgar615.spring.binlog.DmlType;
import com.github.edgar615.util.cache.StartCache;
import com.github.edgar615.util.cache.StartCacheManager;
import com.google.common.base.CaseFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

public class BinlogStartCacheEventListener implements ApplicationListener<DbDataChangedEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(BinlogStartCacheEventListener.class);

  private final StartCacheManager cacheManager;

  public BinlogStartCacheEventListener(StartCacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @Override
  public void onApplicationEvent(DbDataChangedEvent event) {
    DbChangedData dbChangedData = (DbChangedData) event.getSource();
    try {
      String cacheName = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL)
          .convert(dbChangedData.table().toLowerCase()) + "StartCache";
      StartCache startCache = cacheManager.getCache(cacheName);
      if (startCache != null) {
        List elements = dbChangedData.data()
            .stream()
            .map(d -> startCache.transform(d))
            .collect(Collectors.toList());
        if (dbChangedData.type() == DmlType.INSERT) {
          LOGGER.debug("add cache: {}", elements.size());
          startCache.add(elements);
        }
        if (dbChangedData.type() == DmlType.UPDATE) {
          LOGGER.debug("update cache: {}", elements.size());
          startCache.update(elements);
        }
        if (dbChangedData.type() == DmlType.DELETE) {
          LOGGER.debug("delete cache: {}", elements.size());
          startCache.delete(elements);
        }
      }
    } catch (Exception e) {
      LOGGER.error("err", e);
    }
  }


}
