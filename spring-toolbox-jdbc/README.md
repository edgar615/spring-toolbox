自动创建Jdbc的bean
只要存在Datasource就会创建一个Jdbc

默认为每个表匹配 表名+JdbcCache的缓存，可以通过custom-spec动态设置名称，
所以cachemanager要使用CompositeCacheManager并设置fallbackToNoOpCache=true
这样在未找到对应表的缓存时可以忽略缓存。
有时候我们不愿意为每个表写一个XXXJdbcCache，可以通过custom-spec为每个表分别指定前缀

jdbc:
  caching:
    config:
      custom-spec:
        Device:
          cache-name-prefix: "Device"
        DevicePart:
          cache-name-prefix: "test"
