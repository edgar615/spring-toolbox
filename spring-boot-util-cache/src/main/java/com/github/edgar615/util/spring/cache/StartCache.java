package com.github.edgar615.util.spring.cache;

import com.github.edgar615.util.db.Persistent;

import java.util.List;
import java.util.Map;

/**
 * 启动启动时加载到内存中的缓存.
 * 实现一个定时任务，定时执行load刷新数据
 *
 * @author Edgar  Date 2018/5/18
 */
public interface StartCache<ID, T extends Persistent<ID>> {

  /**
   * 缓存的名称
   *
   * @return
   */
  String name();

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
   * 初始化动作.
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

  /**
   * 增加数据
   *
   * @param datas
   */
  void add(List<T> datas);

  /**
   * 增加数据
   *
   * @param data
   */
  void add(T data);

  /**
   * 修改数据
   *
   * @param datas
   */
  void update(List<T> datas);

  /**
   * 修改数据
   *
   * @param data
   */
  void update(T data);

  /**
   * 删除数据
   *
   * @param datas
   */
  void delete(List<T> datas);

  /**
   * 删除数据
   *
   * @param data
   */
  void delete(T data);

  /**
   * 将map对象转换实体.
   * 这个方法主要是给spring-boot-startcache-binlog组件使用的一个辅助方法.
   *
   * @param source
   * @return
   */
  T transform(Map<String, Object> source);
}
