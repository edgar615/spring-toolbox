FixClientInterceptor
固定一个appKey，不做签名计算

```
client:
    fix:
        companyCode: '000001000001' #需要用引号包括，不然spring会将000001000001转换为数字
        appKey: abc
        appSecret: 12345678
        name: infant-api
```

SimpleClientInterceptor
用于微服务的下游计算，在网关中已经对签名做了判断，会将client信息存入请求头x-client-appkey。
SimpleClientInterceptor只需要解析这个请求头即可

ClientInfoInterceptor
用于单体应用的签名校验，应用中需要实现一个ClientFinder的接口。
这个拦截器会根据调用方传入的参数做签名校验
