package com.github.edgar615.util.spring.cache.binlog;

import com.github.edgar615.mysql.mapping.Table;
import com.github.edgar615.mysql.mapping.TableRegistry;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Edgar on 2018/5/22.
 *
 * @author Edgar  Date 2018/5/22
 */
public class TableUtils {
  public static Table getAndCheck(String tableName) {
    Optional<Table> optional =
            TableRegistry.instance().tables().stream()
                    .filter(table -> table.getName()
                            .equalsIgnoreCase(tableName))
                    .findFirst();
    if (!optional.isPresent()) {
//      SystemException exception = SystemException.create(DefaultErrorCode.TARGET_NOT_FOUND)
//              .setDetails("table:" + tableName);
      throw new NoSuchElementException("table:" + tableName);
    }
    return optional.get();
  }

  public static Table get(String tableName) {
    Optional<Table> optional =
            TableRegistry.instance().tables().stream()
                    .filter(table -> table.getName()
                            .equalsIgnoreCase(tableName))
                    .findFirst();
    return optional.orElse(null);
  }
}
