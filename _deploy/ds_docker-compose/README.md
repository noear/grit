# Grit docker-compose 模式部署说明  (for ds)

## 请在完整的看完文档后，再开始动手!!!

## 一、环境要求说明（请准备好）

* mysql8：做存储库（字符集：utf8mb4，排序集：utf8mb4_general_ci）


## 二、部署服务说明
| 服务 | 说明 | 镜像                 |
| -------- | --------  |--------------------| 
| gritdock    | Grit 权限管理、接口服务及菜单导航服务 | noearorg/gritdock:2.0.1 | 

## 三、初始化数据库

* 新建 grit 库 （字符集：utf8mb4，排序集：utf8mb4_general_ci）
* 并执行 grit.sql 初始化脚本

## 四、开始部署服务

新建个目录：grit，把 docker-compose.yml 放进去。然后修改内部的 db 连接配置 

**进入 grit 目录后，开始运行**

```shell
docker-compose up
```


