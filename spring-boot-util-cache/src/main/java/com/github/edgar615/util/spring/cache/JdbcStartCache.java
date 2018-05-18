package com.github.edgar615.util.spring.cache;

import com.github.edgar615.util.db.Jdbc;
import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.search.Example;

import java.util.function.BiConsumer;

/**
 * 从数据库中加载数据的工具类,这个工具类会一直执行，直到load方法返回null或者空
 * Builder必须传入下列参数：
 * jdbc
 * elementType 实体的class
 * example 默认的查询条件，不应该带排序功能，因为这个工具类会用主键生序排列，如果增加了排序，会导致加载的数据不一致.
 * limit 每次加载的数量
 * consumer 加载前对example的动态修改，这个类的主要作用是：在第一次加载全部数据到内存中后，如果每次都重新加载会比较耗时，所以在AbstractStartCache
 * 中记录了上次加载的时间，调用方可以根据上次加载时间缩小加载的范围，例如：lastModifyOn >= lastLoadOn
 *
 * @author Edgar  Date 2018/5/18
 */
public class JdbcStartCache<ID, T extends Persistent<ID>> extends AbstractStartCache<ID, T> {

  private final Jdbc jdbc;

  private final Class<T> elementType;

  private final Example example;

  private final int limit;

  private final BiConsumer<Example, Long> consumer;

  private JdbcStartCache(Jdbc jdbc, Class<T> elementType, Example example, int limit,
                         BiConsumer<Example, Long> consumer) {
    this.jdbc = jdbc;
    this.elementType = elementType;
    this.example = example;
    this.limit = limit;
    this.consumer = consumer;
  }

  public static <ID, T extends Persistent<ID>> Builder<ID, T> builder() {
    return new Builder<>();
  }

  @Override
  public void load() {
    Example newExample = Example.create();
    newExample.addCriteria(example.criteria());
    newExample.addFields(example.fields());
    if (consumer != null) {
      consumer.accept(newExample, lastLoadOn());
    }
    JdbcLoadAllAction.<ID, T>builder()
            .setConsumer(this::handleResult)
            .setJdbc(jdbc)
            .setElementType(elementType)
            .setExample(newExample)
            .setLimit(limit)
            .build()
            .execute();
  }

  public static class Builder<ID, T extends Persistent<ID>> {
    private Jdbc jdbc;

    private Class<T> elementType;

    private Example example;

    private int limit;

    private BiConsumer<Example, Long> consumer;

    public Builder setConsumer(
            BiConsumer<Example, Long> consumer) {
      this.consumer = consumer;
      return this;
    }

    public Builder setJdbc(Jdbc jdbc) {
      this.jdbc = jdbc;
      return this;
    }

    public Builder setElementType(Class<T> elementType) {
      this.elementType = elementType;
      return this;
    }

    public Builder setExample(Example example) {
      this.example = example;
      return this;
    }

    public Builder setLimit(int limit) {
      this.limit = limit;
      return this;
    }

    public JdbcStartCache build() {
      return new JdbcStartCache(jdbc, elementType, example, limit, consumer);
    }
  }
}
