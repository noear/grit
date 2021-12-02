# Grit docker-compose 模式部署说明 (for water)

## 请在完整的看完文档后，再开始动手!!!

## 一、环境要求说明（请准备好）

* water：做为环境支持（内部已存在 Grit 相关配置）

## 二、部署服务说明

| 服务 | 说明 |  镜像 |
| -------- | --------  |  -------- | 
| gritdock    | Grit 权限管理及服务 |  noearorg/gritdock |  


## 三、开始部署服务

新建个目录：grit，把 docker-compose.yml 放进去。然后修改内部的 water 连接配置 

**进入 grit 目录后，开始运行**

```shell
docker-compose up
```


