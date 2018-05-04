package com.github.edgar615.util.spring.eventbus;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix = "eventbus.consumer")
public class EventbusConsumerProperties {

  /**
   * The default number of consumer worker threads to be used  = 2 * number of cores on the machine
   */
  public static final int DEFAULT_WORKER_POOL_SIZE =
          2 * Runtime.getRuntime().availableProcessors();

  private static final boolean DEFAULT_AUTO_COMMIT = false;

  private static final int DEFAULT_SESSION_TIMEOUT_MS = 30000;

  private static final String DEFAULT_AUTO_OFFSET_RESET = "latest";

  private static int DEFAULT_BLOCKER_CHECKER_MS = 1000;

  private static int DEFAULT_MAX_QUOTA = 30000;

  /**
   * 是否自动提交，默认值false
   */
  private final boolean consumerAutoCommit = DEFAULT_AUTO_COMMIT;

  /**
   * 订阅的主题
   */
  private List<String> topics = new ArrayList<>();

  /**
   * 订阅的正则，只要有订阅的主题，正则将不起作用
   */
  private String pattern;

  /**
   * 自动提交的间隔时间，默认值1000
   */
//  private int consumerAutoCommitIntervalMs = DEFAULT_AUTO_COMMIT_INTERVAL_MS;

  /**
   * 消费者session的过期时间，默认值30000
   */
  private int consumerSessionTimeoutMs = DEFAULT_SESSION_TIMEOUT_MS;

  private String consumerAutoOffsetReset = DEFAULT_AUTO_OFFSET_RESET;

  private String servers;

  private String group;

  /**
   * 工作线程数量
   */
  private int workerPoolSize = DEFAULT_WORKER_POOL_SIZE;

  /**
   * 阻塞检查
   */
  private int blockedCheckerMs = DEFAULT_BLOCKER_CHECKER_MS;

  /**
   * 最大配额，当未处理的事件超过配额时，需要暂停消费
   */
  private int maxQuota = DEFAULT_MAX_QUOTA;

  private List<String> offsets = new ArrayList<>();

  public boolean isConsumerAutoCommit() {
    return consumerAutoCommit;
  }

  public List<String> getTopics() {
    return topics;
  }

  public void setTopics(List<String> topics) {
    this.topics = topics;
  }

  public List<String> getOffsets() {
    return offsets;
  }

  public void setOffsets(List<String> offsets) {
    this.offsets = offsets;
  }

  public int getConsumerSessionTimeoutMs() {
    return consumerSessionTimeoutMs;
  }

  public void setConsumerSessionTimeoutMs(int consumerSessionTimeoutMs) {
    this.consumerSessionTimeoutMs = consumerSessionTimeoutMs;
  }

  public String getConsumerAutoOffsetReset() {
    return consumerAutoOffsetReset;
  }

  public void setConsumerAutoOffsetReset(String consumerAutoOffsetReset) {
    this.consumerAutoOffsetReset = consumerAutoOffsetReset;
  }

  public String getServers() {
    return servers;
  }

  public void setServers(String servers) {
    this.servers = servers;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public int getWorkerPoolSize() {
    return workerPoolSize;
  }

  public void setWorkerPoolSize(int workerPoolSize) {
    this.workerPoolSize = workerPoolSize;
  }

  public int getBlockedCheckerMs() {
    return blockedCheckerMs;
  }

  public void setBlockedCheckerMs(int blockedCheckerMs) {
    this.blockedCheckerMs = blockedCheckerMs;
  }

  public int getMaxQuota() {
    return maxQuota;
  }

  public void setMaxQuota(int maxQuota) {
    this.maxQuota = maxQuota;
  }

}
