package com.github.edgar615.spring.cache;

import com.github.edgar615.util.db.Persistent;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 在集群环境下，StartCache没有做数据同步，为了不增加系统的复杂度，不采用消息通知的方式，采用定时查询的方式。
 *
 * 但是由于删除是直接做的物理删除，定时查询根据最近更新时间处理不了删除的数据。
 *
 * 而StartCache里面存储的基本上是极少会变动的数据，如果每次都全量读取数据库有点过于浪费资源。
 *
 * 所以基于spring的event实现了一个扩展，在对缓存的数据操作后，发布一个StartCacheChangedEvent，然后由调用方决定是否对这个事件做处理。
 *
 * 如果性能压力不大，直接用AbstractStartCache，然后全量更新StartCache就可以了。
 *
 * 如果不想全量更新，就可以调用方订阅这个实现，然后将变化操作存放到一个中间表（可以理解为用数据库做的一个队列）其他集群定时加载中间表，然后更新到对应的StartCache
 *
 * PS：中间表的逻辑需要调用方自己编写，实践了一把，实现太过复杂，还是直接丢给
 *
 * @param <ID>
 * @param <T>
 */
public abstract class AbstractPublishChangedStartCache<ID, T extends Persistent<ID>> extends AbstractStartCache<ID, T> {

  private final ApplicationEventPublisher publisher;

  protected AbstractPublishChangedStartCache(
      ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public synchronized void add(List<T> data) {
    // 先发送消息，避免异常
    List<Map<String, Object>> changedData = data.stream()
        .map(r -> r.toMap())
        .collect(Collectors.toList());
    StartCacheChangedData startCacheChangedData = new StartCacheChangedData(name(), ChangedType.INSERT, changedData);
    publisher.publishEvent(new StartCacheChangedEvent(startCacheChangedData));
    super.add(data);
  }

  @Override
  public synchronized void add(T data) {
    StartCacheChangedData startCacheChangedData = new StartCacheChangedData(name(), ChangedType.INSERT,
        Lists.newArrayList(data.toMap()));
    publisher.publishEvent(new StartCacheChangedEvent(startCacheChangedData));
    super.add(data);
  }

  @Override
  public synchronized void update(List<T> data) {
    List<Map<String, Object>> changedData = data.stream()
        .map(r -> r.toMap())
        .collect(Collectors.toList());
    StartCacheChangedData startCacheChangedData = new StartCacheChangedData(name(), ChangedType.UPDATE, changedData);
    publisher.publishEvent(new StartCacheChangedEvent(startCacheChangedData));
    super.update(data);
  }

  @Override
  public synchronized void update(T data) {
    StartCacheChangedData startCacheChangedData = new StartCacheChangedData(name(), ChangedType.UPDATE,
        Lists.newArrayList(data.toMap()));
    publisher.publishEvent(new StartCacheChangedEvent(startCacheChangedData));
    super.update(data);
  }

  @Override
  public synchronized void delete(List<T> data) {
    List<Map<String, Object>> changedData = data.stream()
        .map(r -> r.toMap())
        .collect(Collectors.toList());
    StartCacheChangedData startCacheChangedData = new StartCacheChangedData(name(), ChangedType.DELETE, changedData);
    publisher.publishEvent(new StartCacheChangedEvent(startCacheChangedData));
    super.delete(data);
  }

  @Override
  public synchronized void delete(T data) {
    StartCacheChangedData startCacheChangedData = new StartCacheChangedData(name(), ChangedType.DELETE,
        Lists.newArrayList(data.toMap()));
    publisher.publishEvent(new StartCacheChangedEvent(startCacheChangedData));
    super.delete(data);
  }

}
