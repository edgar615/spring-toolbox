
```
CREATE TABLE `distributed_lock` (
`id` BIGINT ( 20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`lock_key` VARCHAR ( 64 ) NOT NULL COMMENT '锁定的资源',
`lock_value` VARCHAR ( 255 ) NOT NULL COMMENT '锁的客户端标识',
`locked_at` BIGINT ( 20 ) NOT NULL COMMENT '锁创建时间，单位毫秒',
`expire_at` BIGINT ( 20 ) NOT NULL COMMENT '锁过期时间，单位毫秒',
`update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '保存数据时间，自动生成',
PRIMARY KEY ( `id` ),
UNIQUE KEY `uidx_lock_key` ( `lock_key` ) USING BTREE,
KEY `idx_expire_at` ( `expire_at` ) USING BTREE 
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '分布式锁';
```
