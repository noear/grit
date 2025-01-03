#### 2.0.1

* 添加 solonx-licence 控制

#### 2.0.0

* solon 升为 3.0.5
* wood 升为 1.3.16
* snack3 升为 3.2.122
* 调整模块名
  * grit.client -> grit-client
  * grit.server -> grit-server-api
  * grit.server.ui-durian -> grit-server-ui-durian
* 新增模块
  * grit-server-solon-plugin

* 配置名调整

| 旧名             | 新名                  | 备注                       |
|----------------|---------------------|--------------------------|
| `grit.server`  | `gritclient.server` | 突出客户端语义（`grit.` 留给服务端专用） |
| `grit.token`   | `gritclient.token`  |                          |
| `grit.token`   | `grit.api.token`    | 突出突出 api 语义              |



#### 1.10.2
* solon 升为 3.0.3
* wood 升为 1.3.14
* snack3 升为 3.2.121

#### 1.10.0
* solon 升为 3.0.0

#### 1.9.2
* solon 升为 2.9.2

#### 1.8.3
* solon 升为 2.7.1

#### 1.8.2
* 简化 gritdock 编辑界面

#### 1.8.1
* 优化几个空的判断处理

#### 1.8.0
* solon 升为 2.7.0

#### 1.7.1
* solon 升为 2.6.6
* wood 升为 1.2.6
* snack3 升为 3.2.88

#### 1.7.0
* solon 升级为 2.6.0

#### 1.6.6
* solon 升级为 2.5.7
* wood 升级为 1.2.2
* snack3 升级为 3.2.80
* water 升级为：2.11.4
* 调整视图目录为 `resources/WEB-INF/templates/`

#### 1.6.3
* solon 升级为 2.4.2
* wood 升级为 1.1.8
* snack3 升级为 3.2.75
* water 升级为：2.11.2

#### 1.6.1
* solon 升级为 2.3.7
* wood 升级为 1.1.5

#### 1.6.0
* solon 升级为 2.3.0
* snack3 升级为 3.2.72
* wood 升级为 1.1.2
* slf4j 升级为 2.x

#### 1.5.5
* solon 升级为：2.2.17

#### 1.5.3
* solon 升级为：2.2.15
* snack3 升级为：3.2.66
* wood 升级为：1.1.1

#### 1.5.2
* solon 升级为：2.2.8
* snack3 升级为：3.2.62
* wood 升级为：1.1.0
* water 升级为：2.10.3

#### 1.5.0
* solon 升级为：2.0.1

#### 1.4.4
* solon 升级为：1.12.2（修复 1.11.5+ water 适配不能同步状态的问题）

#### 1.4.3
* solon 升级为：1.11.5
* snack3 升级为：3.2.50
* wood 升级为：1.0.7

#### 1.4.2
* gritdock 改由 smart-http 驱动
* solon 升级为：1.10.13
* snack3 升级为：3.2.46

#### 1.4.1
* solon 升级为：1.10.11

#### 1.4.0
* 取消 weed3 ，改用 wood

#### 1.3.2
* 解决 ldap 登录时，无法自动创建空账号的问题

#### 1.3.1
* solon 升级为：1.9.3
* water 升到为：2.8.1

#### 1.3.0
* solon 升级为：1.9.1
* water 升到为：2.8.0（需要 water server 2.8.0 配套）
* snack3 升级为：3.2.31

#### 1.2.3
* 增加限定授权支持（限定某资源空间）

#### 1.2.2
* 升级 solon 到 1.9.0

#### 1.2.1
* 升级 solon 到 1.8.2

#### 1.2.0
* 升级 solon 到 1.8.0

#### 1.1.4
* 优化ui界面

#### 1.1.3
* "grit-init.jsond" 增加json内容支持

#### 1.1.1
* 升级 solon 到 1.7.2
* 升级 water 到 2.6.1
* 升级 weed3 到 3.4.22
* 升级 sanck3 到 3.2.20

#### 1.0.11
* 升级 solon 到 1.6.36

#### 1.0.10
* 优化菜单初始化能力

#### 1.0.9
* 升级 solon 到 1.6.32
* 资源表、主体表，增加 guid 字段
* 资源组、资源实体，支持导入导出和批量操作

#### 1.0.8
* 升级 solon 到 1.6.30

#### 1.0.7
* 升级 solon 到 1.6.20
* 空间增加"是否全屏"属性

#### 1.0.6
* 升级 solon 到 1.6.19
* 升级 weed3 到 3.4.16，解决loap登录时自动创建用户失败的问题

#### 1.0.3

* gritdock 增加rpc接口日志
* grit.client 增加全局过滤器支持，进而实现链路跟踪

#### 1.0.2

* SessonBase 增加 clear 接口。可不清除 grit admin token