## 两种部署模式：

### 一、基于配置服务部署（以 water 为例）

* water_docker-compose ：基于 water 的 docker-compose 部署模式
* water_k8s ：基于 water 的 k8s 部署模式

### 二、基于数据源部署

请自建数据库：grit，并通过 grit.sql 初始化数据结构

* ds_docker-compose ：基于数据源部署的 docker-compose 部署模式
* ds_k8s ：基于数据源部署的 k8s 部署模式


## 镜像列表

| 镜像                 | 镜像端口    | 说明        |
|--------------------|-------|-----------|
| noearorg/gritdock:2.0.1 | 7281  | 管理控制台     |

## 附：LDAP登录支持：

可以放到配置服务里，也可做为外部配置（或环境变量）

```yaml
# 配置项 grit/grit.yml ，添加 ldap 连接配置：
grit.ldap:
  url: "ldap://127.0.0.1"
  baseDn: "DC=company,DC=com"
  bindDn: "cn=admin,dc=company,dc=com"
  paasword: "123456"
  userFilter: "cn=%s"
  groupFilter: "cn=%s"
```


