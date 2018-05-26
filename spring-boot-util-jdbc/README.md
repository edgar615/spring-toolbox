自动创建Jdbc的bean
只要存在Datasource就会创建一个Jdbc

有时候我们希望给JDBC增加一个缓存，对于一些简单的CRUD如果每次都在service上加太麻烦，所以我创建了一个CacheWrappedJdbc对象，可以为某些表增加缓存

如果开启了配置
```
jdbc.cache.enable=true
```
而且存在Datasource和CacheManager这两个bean

***需要在CacheManager中增加 xxxxJdbcCache的缓存*，例如sys_user表的cache名称应该是SysUserJdbcCache
