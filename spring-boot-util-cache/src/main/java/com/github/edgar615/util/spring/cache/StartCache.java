package com.github.edgar615.util.spring.cache;

import com.github.edgar615.util.db.Persistent;

import java.util.List;

/**
 * 启动启动时加载到内存中的缓存.
 * 实现一个定时任务，定时执行load刷新数据
 *
 * @author Edgar  Date 2018/5/18
 */
public interface StartCache<ID, T extends Persistent<ID>> {

  /**
   * 查询列表.
   * <b>数组中的元素如果发生更改会引起数据不一致</b>
   *
   * @return
   */
  List<T> elements();

  /**
   * 加载数据
   */
  void load();

  /**
   * 根据ID查询
   * <b>元素如果发生更改会引起数据不一致</b>
   *
   * @param id
   * @return
   */
  T get(ID id);
}
