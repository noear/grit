# Grit jar 模式部署或运行说明  (for ds)

## 请在完整的看完文档后，再开始动手!!!

## 一、环境要求说明（请准备好）

* mysql8：做存储库（字符集：utf8mb4，排序集：utf8mb4_general_ci）


## 二、初始化数据库

* 新建 grit 库 （字符集：utf8mb4，排序集：utf8mb4_general_ci）
* 并执行 grit.sql 初始化脚本

## 三、jar 运行

* 添加配置

在 ./gritdock_ext/app.yml 文件里添加配置（如果没有，则新建文件）

```yaml
grit.db.schema: grit
grit.db.server: mysql.grit.io:3306
grit.db.password: 123456
grit.db.username: demo
grit.token: B6uWZDYUm4kMscdEAERXQ2wMBW1nLL0j   #rpc连接令牌（只要前后能对起来，随便输）
gritadmin.password: SykSYLWN9WTpzCHq           # Grit 管理密码
```

* 运行 

```
java -jar gritdock.jar
```


## 四、源码运行（只适合临时调试）

* 添加配置

在 app.yml 资源文件里添加配置

```yaml
grit.db.schema: grit
grit.db.server: mysql.grit.io:3306
grit.db.password: 123456
grit.db.username: demo
grit.token: B6uWZDYUm4kMscdEAERXQ2wMBW1nLL0j   #rpc连接令牌（只要前后能对起来，随便输）
gritadmin.password: SykSYLWN9WTpzCHq           # Grit 管理密码
```

* 运行

运行 `DocakApp::main()` 函数


