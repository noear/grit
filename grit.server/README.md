
可以集成到别的服务里，做为接口的一部份

* 比如集成到 waterapi ，做为 water 服务的一部份


配置示例，内容视情况改动

* grit.yml，管理端用

```properties
grit.cache.driverType=local

grit.db.schema=water
grit.db.url=jdbc:mysql://mysql.water.io:3306/water?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true
grit.db.password=123456
grit.db.username=demo

grit.token=GmatSe7QJeZpmRmP1sltJ8p1sbNpVEpj

grit.admin.user=admin
grit.admin.password=4rvCrCQcFWkxK89G
```

* gritclient.yml，客户端用

```properties
grit.server=http://localhost:7281
grit.token=B6uWZDYUm4kMscdEAERXQ2wMBW1nLL0j

server.session.state.domain=water.noear.org
server.session.timeout=7200
```