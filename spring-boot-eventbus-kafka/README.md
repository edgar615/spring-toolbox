自动注入KAFKA的EventbusProducer和EventConsumer

如果设置了
`eventbus.consumer.group`和`eventbus.consumer.servers`这两个属性，会自动注入EventConsumer

```
  @ConditionalOnProperty(prefix = "eventbus.consumer", name = {"servers", "group"},
          matchIfMissing = false)
  @ConditionalOnMissingBean(EventConsumer.class)
```

- eventbus.consumer.group`消费者组
- eventbus.consumer.servers kafka的地址
- eventbus.consumer.pattern 使用正则表达式匹配主题，只要设置了topics属性，正则将不起作用
- eventbus.consumer.topics 订阅的主题列表 
- eventbus.consumer.consumerAutoCommit 是否自动提交 默认true
- eventbus.consumer.consumerSessionTimeoutMs 消费者session的过期时间，默认值30000
- eventbus.consumer.consumerAutoOffsetReset 判断偏移量的方式，默认earliest
    - earliest 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
    - latest 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
    - none topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
- eventbus.consumer.workerPoolSize 工作线程的数量 默认CPU*2
- eventbus.consumer.blockedCheckerMs 消息从开始处理到最后完成，超过多少毫秒被认为阻塞 默认 1000
- eventbus.consumer.maxQuota 内部队列的最大长度，超过这个长度后队列将停止消费
- eventbus.consumer.offsets 自定义读取的偏移量, 格式为 topic,partition,offset, **慎用这个属性**

如果需要开启持久化，就需要创建ConsumerStorage这个Bean
如果需要黑名单，就需要创建BlackListFilter这个Bean
如果需要顺序处理，就需要创建IdentificationExtractor这个Bean

如果设置了·eventbus.producer.servers`这个属性，会自动注入EventProducer

```
  @ConditionalOnProperty(prefix = "eventbus.producer", name = "servers", matchIfMissing = false)
  @ConditionalOnMissingBean(KafkaEventProducer.class)
```

- eventbus.producer.servers kafka的地址
- eventbus.producer.retries 重试次数，默认0
- eventbus.producer.maxQuota 最大配额，超过这个配额将抛出异常，默认30000
- eventbus.producer.partitionClass 分区类，默认为null

如果需要开启持久化，就需要创建ProducerStorage这个Bean
