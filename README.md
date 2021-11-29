# Grit

权限管理系统，或者资源授权管理中心


### 工件输出：

| 开发框架 | 说明 | 
| -------- | -------- | 
| grit.client     | grit 接口申明及数据实体定义。支持Remote调用与Local调用     | 
| grit-solon-solon     | grit.client 与 solon.auth 集成    | 
| grit.server     | grit.client 接口实现（可做为插件集成到别的服务）     | 
| grit.server.ui     | grit.server + 管理界面实现（可做为插件集成到别的管理后台）     | 


| 服务输出 | 说明 | 
| -------- | -------- | 
| gritapi     | grit.client 接口服务（集成grit.server）     | 
| gritadmin     | gritapi + 管理界面（grit.server.ui）    | 
| gritdock     | grit.client + 多系统菜单导航     | 


### 存储设计:

* 资源表。树形，分三个领域概念：
  * 资源空间
  * 资源组
  * 资源（或叫：资源实体）
    * 可见（菜单）
    * 不可见（权限）
* 资源连接表
  * 资源->主体
* 主体表。树形，分二个领域概念：
  * 主体组
    * 可见（分组）
    * 不可见（角色）
  * 主体（或叫：主体实体）
* 主体连接表
  * 主体->主体组