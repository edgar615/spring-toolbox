package com.github.edgar615.spring.cache;

import com.github.edgar615.util.db.Persistent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/5/18.
 *
 * @author Edgar  Date 2018/5/18
 */
public abstract class AbstractStartCache<ID, T extends Persistent<ID>>
        implements StartCache<ID, T> {
  private final List<T> elements = new CopyOnWriteArrayList<>();

  @Override
  public synchronized void add(List<T> data) {
    Objects.requireNonNull(data);
    elements.addAll(data);
  }

  @Override
  public synchronized void add(T data) {
    Objects.requireNonNull(data);
    elements.add(data);
  }

  @Override
  public synchronized void update(List<T> data) {
    delete(data);
    add(data);
  }

  @Override
  public synchronized void update(T data) {
    delete(data);
    add(data);
  }

  @Override
  public synchronized void delete(List<T> data) {
    Objects.requireNonNull(data);
    List<ID> ids = data.stream()
            .map(cs -> cs.id())
            .collect(Collectors.toList());
    elements.removeIf(e -> ids.contains(e.id()));
  }

  @Override
  public synchronized void delete(T data) {
    Objects.requireNonNull(data);
    elements.removeIf(e -> data.id().equals(e.id()));
  }

  @Override
  public List<T> elements() {
    return new ArrayList<>(elements);
  }

  @Override
  public T get(ID id) {
    return elements.stream().filter(e -> e.id().equals(id))
            .findFirst().orElse(null);
  }

  @Override
  public void clear() {
    this.elements.clear();
  }
}
