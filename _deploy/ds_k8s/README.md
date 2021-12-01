# Water k8s 模式部署说明

## 请在完整的看完文档后，再开始动手!!!

## 一、环境要求说明（请准备好）

* mysql8：做为主库（字符集：utf8mb4，排序集：utf8mb4_general_ci）
* redis：做为分布式锁、数据临时队列用
* mongodb：做为消息持久化用（也可以使用 mysql8）
* elasticsearch：做为日志持久化用（也可以使用 mysql8 或 mongodb）

## 二、初始化环境

运行 Water 助理（在本地或服务器上运行都可）

```shell
docker run -it --rm -p 19371:19371 noearorg/wateraide
```

* 用浏览器打开界面：`http://locahost:19371`，按提示操作

* 完成操作后，关掉服务(有需要再启动，每次用完都关掉)

## 三、开始部署服务

* 添加 water 域

* 添加 water/waterapi 服务（镜像：noearorg/waterapi:latest）。主接口
  * 镜像端口：9371
  * 建议2个副本起步
  

  ```properties
  #添加环境变量（替换为初始化好的 Water DB 配置）：
  water.ds.schema=water
  water.ds.server=mysql.water.io:3306
  water.ds.username=demo
  water.ds.password=123456
  ```

* 添加 water/wateradmin 服务（镜像：noearorg/wateradmin:latest）。管理控制台
  * 镜像端口：8080
  * 建议1个副本即可
  * 要配置外网访问地址，建议加域名

  
