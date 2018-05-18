package com.github.edgar615.util.spring.cache;

import com.github.edgar615.util.db.Jdbc;
import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.search.Example;

import java.util.function.BiConsumer;

/**
 * Created by Edgar on 2018/5/18.
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
    consumer.accept(newExample, lastLoadOn());
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
