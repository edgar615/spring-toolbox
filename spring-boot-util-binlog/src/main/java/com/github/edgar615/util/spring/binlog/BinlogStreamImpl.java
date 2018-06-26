package com.github.edgar615.util.spring.binlog;

import com.google.common.base.Splitter;

import com.github.edgar615.mysql.mapping.Table;
import com.github.edgar615.mysql.mapping.TableMapping;
import com.github.edgar615.mysql.mapping.TableMappingOptions;
import com.github.edgar615.mysql.mapping.TableRegistry;
import com.github.edgar615.util.concurrent.NamedThreadFactory;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 读取binlog，更新缓存的工具，临时写的，比较ugly，后面再提炼.
 * 约定StartCache的名称都是 表名的小写驼峰+"StartCache"组成。
 *
 * @author Edgar  Date 2018/5/22
 */
class BinlogStreamImpl implements BinlogStream, ApplicationEventPublisherAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(BinlogStreamImpl.class);

  private final ExecutorService executorService;

  private final BinlogProperties binlogProperties;

  private final DataSourceProperties dataSourceProperties;

  private final String database;

  private final String jdbcArg;

  private final String host;

  private final int port;

  private ApplicationEventPublisher publisher;

  BinlogStreamImpl(BinlogProperties binlogProperties, DataSourceProperties dataSourceProperties) {
    this.binlogProperties = binlogProperties;
    this.dataSourceProperties = dataSourceProperties;
    this.executorService =
            Executors.newFixedThreadPool(1, NamedThreadFactory.create("mysql-binlog-reader"));
    String url = dataSourceProperties.getUrl();
    int argIndex = url.indexOf("?");
    this.jdbcArg = url.substring(argIndex + 1);
    List<String> hostPortAndDb
            = Splitter.on("/")
            .splitToList(url.substring("jdbc:mysql://".length(), argIndex));
    this.database = hostPortAndDb.get(1);
    List<String> hostAndPort = Splitter.on(":").splitToList(hostPortAndDb.get(0));
    this.host = hostAndPort.get(0);
    this.port = Integer.parseInt(hostAndPort.get(1));
  }

  @Override
  public void close() {
    executorService.shutdownNow();
  }

  @Override
  public void start() {
    executorService.submit(() -> {
      try {
        TableMappingOptions options = new TableMappingOptions()
                .setDatabase(database)
                .setHost(host)
                .setPort(port)
                .setUsername(dataSourceProperties.getUsername())
                .setPassword(dataSourceProperties.getPassword())
                .setJdbcArg(jdbcArg)
                .addGenTables(binlogProperties.getTables());
        TableMapping mapping = new TableMapping(options);
        List<Table> tables = mapping.fetchTable();
        TableRegistry.instance().addAll(tables);
        Pipe pipe = new Pipe(new CachedDataConsumer());
        BinaryLogClient client =
                new BinaryLogClient(host, port, binlogProperties.getUsername(),
                                    binlogProperties.getPassword());
        client.registerEventListener(event -> pipe.add(event));
        client.connect();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.publisher = applicationEventPublisher;
  }

  private DbChangedData transform(DmlData dmlData) {
    List<Map<String, Object>> data = dmlData.data().stream()
            .map(m -> TableUtils.parseSource(dmlData.table(), m))
            .collect(Collectors.toList());
    return DbChangedData.create(dmlData.database(), dmlData.table(), dmlData.type(), data);
  }

  public class CachedDataConsumer implements Consumer<List<DmlData>> {

    @Override
    public void accept(List<DmlData> dmlDatas) {
      for (DmlData dmlData : dmlDatas) {
        //商品的缓存
        if (dmlData.database().equals(database)
            && binlogProperties.getTables().contains(dmlData.table())) {
          try {
            publisher.publishEvent(DbDataChangedEvent.create(transform(dmlData)));
          } catch (Exception e) {
            LOGGER.error("binlog occur exception", e);
          }
        }
      }
    }

  }
}
