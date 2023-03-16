# Grit docker-compose 模式部署说明 (for water)

## 请在完整的看完文档后，再开始动手!!!

## 一、环境要求说明（请准备好）

* water：做为环境支持（内部已存在 Grit 相关配置）

## 二、部署服务说明

| 服务 | 说明 | 镜像                 |
| -------- | --------  |--------------------| 
| gritdock    | Grit 权限管理、接口服务及菜单导航服务 | noearorg/gritdock:1.5.1 |  


## 三、开始部署服务

新建个目录：grit，把 docker-compose.yml 放进去。然后修改内部的 water 连接配置 

**进入 grit 目录后，开始运行**

```shell
docker-compose up
```



## 其它

一般在 water 环境下，需要部署 gritdock 时，应该已经部署了 sponge 中台。所以需要它做统一管理。

建议操作顺序：

* 进入 http://x.x.x/grit/ ，先 wateradmin 和 spongeadmin 的资源空间地址
* 修改 water 配置管理/属性配置 grit/gritclient.yml 的 server.session.state.domain 值为 多系统公共的根域名
* 打开 http://x.x.x ，进入跨系统通用管理平台（也可理解为：多系统功能导航）
* 进入 water 配置管理/属性配置 grit/gritdock.yml，可修改相关标题


同时要求 gritdock, wateradmin, spongeadmin 在同一个根域或二级域下，例：

* gritdock 服务：http://admin.dev.x.x
* wateradmin 服务：http://admin.water.dev.x.x
* spongeadmin 服务：http://admin.sponge.dev.x.x

那它们的共同二级域为：de


