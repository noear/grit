# Grit k8s 模式部署说明 (for water)

## 请在完整的看完文档后，再开始动手!!!

## 一、环境要求说明（请准备好）

* water：做为环境支持（内部已存在 Grit 相关配置）


## 二、部署服务说明

| 服务 | 说明                  |  镜像 |
| -------- |---------------------|  -------- | 
| gritadmin   | Grit 权限管理，并提供远程接口服务 |  noearorg/gritadmin | 
| gritdock    | 多系统菜单导航平台           |  noearorg/gritdock | 


## 三、开始部署服务

* 添加 water/gritadmin 服务（镜像：noearorg/gritadmin:latest）
  * 镜像端口：7281
  * 对外端口：7281
  * 建议2个副本起步
  * 要配置外网访问地址，建议加域名

* 添加 water/gritdock 服务（镜像：noearorg/gritdock:latest）
  * 镜像端口：8080
  * 建议1个副本即可
  * 要配置外网访问地址，建议加域名
  