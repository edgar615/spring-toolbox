package com.github.edgar615.util.spring.cache;

import com.github.edgar615.util.db.Persistent;

import java.util.List;

/**
 * 启动启动时加载到内存中的缓存.
 * 实现一个定时任务，定时执行load刷新数据
 * /(ㄒoㄒ)/~~这个模块设计的时候忘记考虑删除数据的问题了，不想改了。慎用.
 *
 * @author Edgar  Date 2018/5/18
 */
@Deprecated
public interface StartCache<ID, T extends Persistent<ID>> {

  /**
   * 清除缓存
   */
  void clear();

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
