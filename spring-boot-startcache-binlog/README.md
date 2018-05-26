StartCache的更新可以通过定时任务重新从数据库中加载，稍微会有延时。而且除非重新加载，否则无法知道哪些数据需要删除。
所以通过读取binlog来实现这个功能。

```
startcache.binlog.enabled=true
startcache.binlog.username=从库的用户名
startcache.binlog.password=从库的密码
startcache.binlog.tables=[] 哪些表作为StartCache

spring.datasource.url=jdbcurl
spring.datasource.username=数据库用户名
spring.datasource.password=数据库密码
```