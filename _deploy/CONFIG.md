
### for server（建议将此配置放到配置中心, key=grit/grit.yml）

```yaml
grit.db:
  schema: "grit"
  server: "mysql.water.io:3306"
  password: "123456"
  username: "demo"

# cache 配置(可选)
grit.cache:
  driverType: "local"

# ldap 配置(可选)
grit.ldap:
  url: "ldap://127.0.0.1:389"
  baseDn: "DC=company,DC=com"
  bindDn: "cn=admin,dc=company,dc=com"
  paasword: "123456"
  userFilter: "cn=%s"
  groupFilter: "cn=%s"

```

### for client（建议将此配置放到配置中心, key=grit/gritclient.yml）

```yaml
grit.server: "gritapi:7281"
grit.token: "B6uWZDYUm4kMscdEAERXQ2wMBW1nLL0j"

server.session:
  state.domain: "grit.noear.org"
  timeout: "7200"
```




### 附录：存储设计：

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


