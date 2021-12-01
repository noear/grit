

## 两种部署模式：

### 一、基于 water 环境部署

* water_docker-compose ：基于 water 的 docker-compose 部署模式
* water_k8s ：基于 water 的 k8s 部署模式

### 二、基于数据源部署

请自建数据库：grit，并通过 grit.sql 初始化数据结构

* ds_docker-compose ：基于数据源部署的 docker-compose 部署模式
* ds_k8s ：基于数据源部署的 k8s 部署模式


## 附：LDAP登录支持：

* 基于 water 环境

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

* 自建数据源

```
将上面的配置做为 gritadmin 的环境变量添加即可
```

