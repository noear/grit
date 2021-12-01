# Grit k8s 模式部署说明 (for ds)


## 一、环境要求说明（请准备好）

* mysql8：做存储库（字符集：utf8mb4，排序集：utf8mb4_general_ci）


## 二、部署服务说明

| 服务 | 说明                             |  镜像 |
| -------- |--------------------------------|  -------- | 
| gritadmin   | Grit 权限管理，并提供远程接口服务            |  noearorg/gritadmin | 
| gritdock    | 多系统菜单导航平台（可做为演示账号与权限效果；也可以去掉） |  noearorg/gritdock | 

## 三、初始化数据库

* 新建 grit 库 （字符集：utf8mb4，排序集：utf8mb4_general_ci）
* 并执行 grit.sql 初始化脚本

## 四、开始部署服务

* 添加 water/gritadmin 服务（镜像：noearorg/gritadmin:latest）
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
  grit.token=B6uWZDYUm4kMscdEAERXQ2wMBW1nLL0j   #rpc连接令牌
  gritadmin.password=SykSYLWN9WTpzCHq   
  ```

* 添加 water/gritdock 服务（镜像：noearorg/gritdock:latest）
  * 镜像端口：8080
  * 对外端口：8080 或其它
  * 建议1个副本即可
  * 要配置外网访问地址，建议加域名


  ```properties
  #添加环境变量：
  grit.server=gritadmin:7281 
  grit.token=B6uWZDYUm4kMscdEAERXQ2wMBW1nLL0j
  ```
