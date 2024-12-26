# Grit k8s 模式部署说明 (for ds)


## 一、环境要求说明（请准备好）

* mysql8：做存储库（字符集：utf8mb4，排序集：utf8mb4_general_ci）


## 二、部署服务说明

| 服务 | 说明 |  镜像 |
| -------- | --------  |  -------- | 
| gritdock    | Grit 权限管理、接口服务及菜单导航服务 |  noearorg/gritdock | 

## 三、初始化数据库

* 新建 grit 库 （字符集：utf8mb4，排序集：utf8mb4_general_ci）
* 并执行 grit.sql 初始化脚本

## 四、开始部署服务

* 添加 water/gritdock 服务（镜像：noearorg/gritdock:2.0.0）
  * 镜像端口：7281
  * 对外端口：7281
  * 建议2个副本起步
  * 要配置外网访问地址，建议加域名
  

  ```properties
  #添加环境变量（替换为初始化好的 Grit DB 配置）：
  grit.db.schema=grit
  grit.db.server=mysql.grit.io:3306
  grit.db.password=123456
  grit.db.username=demo
  grit.token=B6uWZDYUm4kMscdEAERXQ2wMBW1nLL0j   #rpc连接令牌（只要前后能对起来，随便输）
  gritadmin.password=SykSYLWN9WTpzCHq   
  ```

## 其它

* 打开 http://localhost:7281/grit/ ，可进入权限管理控制台
* 打开 http://localhost:7281/ ，可查看主体的权限效果
