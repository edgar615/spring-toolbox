package com.github.edgar615.util.spring.cache;

import com.github.edgar615.util.db.Persistent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Edgar on 2018/5/18.
 *
 * @author Edgar  Date 2018/5/18
 */
public abstract class AbstractStartCache<ID, T extends Persistent<ID>>
        implements StartCache<ID, T> {
  private final List<T> elements = new CopyOnWriteArrayList<>();

  private volatile long lastLoadOn = 0;

  protected synchronized void handleResult(List<T> result) {
    elements.removeIf(e -> result.stream()
            .anyMatch(r -> r.id().equals(e.id())));
    elements.addAll(result);
    lastLoadOn = Instant.now().getEpochSecond();
  }

  public List<T> elements() {
    return new ArrayList<>(elements);
  }

  public T get(ID id) {
    return elements.stream().filter(e -> e.id().equals(id))
            .findFirst().orElseGet(null);
  }

  protected long lastLoadOn() {
    return lastLoadOn;
  }
}
