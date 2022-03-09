
#### for server（建议将此配置放到配置中心, key=grit/grit.yml）

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

#### for client（建议将此配置放到配置中心, key=grit/gritclient.yml）

```yaml
grit.server: "gritapi:7281"
grit.token: "B6uWZDYUm4kMscdEAERXQ2wMBW1nLL0j"

server.session:
  state.domain: "grit.noear.org"
  timeout: "7200"
```

