package com.github.edgar615.spring.eventbus;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix="eventbus.producer")
public class EventbusProducerProperties {

  private String servers;

  public static final int DEFAULT_LINGER_MS = 1;

  private static final int DEFAULT_BATCH_SIZE = 16384;

  private static final int DEFAULT_BUFFER_MEMORY = 33554432;

  private static final int DEFAULT_RETRIES = 0;

  private static final String DEFAULT_ACKS = "all";

  private static String DEFAULT_PARTITION_CLASS = null;

  private static long DEFAULT_PERIOD = 5 * 60 * 1000;

  private static int DEFAULT_MAX_QUOTA = 30000;

  /**
   * 最大配额，当未处理的事件超过配额时，需要拒绝发送
   */
  private long maxQuota = DEFAULT_MAX_QUOTA;

  /**
   * 从存储层查询待发送事件的间隔，单位毫秒
   */
  private long fetchPendingPeriod = DEFAULT_PERIOD;

  /**
   * 响应,可选值 0, 1, all
   */
  private String acks = DEFAULT_ACKS;

  /**
   * 批量提交的大小
   */
  private int batchSize = DEFAULT_BATCH_SIZE;

  /**
   * 批量提交的大小
   */
  private int bufferMemory = DEFAULT_BUFFER_MEMORY;

  /**
   * 重试的次数
   */
  private int retries = DEFAULT_RETRIES;

  private int lingerMs = DEFAULT_LINGER_MS;

  /**
   * 分区类
   */
  private String partitionClass = DEFAULT_PARTITION_CLASS;

  public String getServers() {
    return servers;
  }

  public void setServers(String servers) {
    this.servers = servers;
  }

  public String getAcks() {
    return acks;
  }

  public void setAcks(String acks) {
    this.acks = acks;
  }

  public int getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public int getBufferMemory() {
    return bufferMemory;
  }

  public void setBufferMemory(int bufferMemory) {
    this.bufferMemory = bufferMemory;
  }

  public int getRetries() {
    return retries;
  }

  public void setRetries(int retries) {
    this.retries = retries;
  }

  public int getLingerMs() {
    return lingerMs;
  }

  public void setLingerMs(int lingerMs) {
    this.lingerMs = lingerMs;
  }

  public String getPartitionClass() {
    return partitionClass;
  }

  public void setPartitionClass(String partitionClass) {
    this.partitionClass = partitionClass;
  }

  public long getMaxQuota() {
    return maxQuota;
  }

  public void setMaxQuota(long maxQuota) {
    this.maxQuota = maxQuota;
  }

  public long getFetchPendingPeriod() {
    return fetchPendingPeriod;
  }

  public void setFetchPendingPeriod(long fetchPendingPeriod) {
    this.fetchPendingPeriod = fetchPendingPeriod;
  }
}
