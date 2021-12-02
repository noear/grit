[![Maven Central](https://img.shields.io/maven-central/v/org.noear/grit.client.svg)](https://search.maven.org/search?q=g:org.noear%20AND%20grit)

` QQ交流群：22200020 `


# Grit（聚沙成堆...）

权限管理系统，或者资源授权管理中心，或者权限控制中台


## 开始

### 了解框架与构件

| 开发框架                  | 说明 | 
|-----------------------| -------- | 
| grit.client           | grit 接口申明及数据实体定义。支持Remote调用与Local调用     | 
| grit-solon-solon      | grit.client 与 solon.auth 集成    | 
| grit.server           | grit.client 接口实现（可做为插件集成到别的服务）     | 
| grit.server.ui-durian | grit.server + 管理界面实现（可做为插件集成到别的管理后台）     | 


| 镜像输出               | 说明     | 
|--------------------|--------| 
| noearorg/gritdock | grit服务 | 


单体项目，可通过 grit.server.ui-durian 集成，直接获取客户端接口与管理能力

### 控制台演示站

地址： [http://grit.noear.org/grit/](http://grit.noear.org/grit/)  （账号：admin ；密码：UjKeQwBK1oCUOCvl ）

**在里面配置权限之后，可以在此处看效果：**

地址：[http://grit.noear.org/](http://grit.noear.org/)  （已有演示主体，账号：admin ；密码：admin ）

### 视频教程

暂无

### (一) 使用

#### 配置
* pom.xml / mevan 配置
```xml
<!-- 客户端版本 -->
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>grit.client</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- solon cloud 集成版本（也可用于 Spring boot 项目） -->
<!-- 用的时候再加个配置服务的插件，例：water-solon-plugin -->
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>grit-solon-plugin</artifactId>
    <version>1.0.0</version>
</dependency>
```

* app.yml / 配置说明
```yml
solon.app:
  name: "demoadmin"
  group: "demo"

solon.cloud.water:
  server: "waterapi:9371"           #WATER服务地址
  config:
    load: "grit:gritclient.yml"     #加载gritclient的配置
```

#### 代码

```java
//定义认证处理器
public class AuthProcessorImpl extends GritAuthProcessor {
    
}

//配置验证规则
@Configuration
public class DemoConfig{
    @Bean
    public void initAuth() {
        //适配认证框架
        AuthUtil.adapter()
                .loginUrl("/login")
                .addRule(r -> r.include("**").verifyIp().failure((c, t) -> c.output(c.realIp() + ", not whitelist"))) //增加ip白名单验证规则
                .addRule(r -> r.exclude("/login**").exclude(HealthHandler.HANDLER_PATH).exclude("/_**").verifyPath()) //增加uri验证规则
                .processor(new AuthProcessorImpl()) //绑定验证处理器
                .failure((ctx, rst) -> {
                    ctx.outputAsJson(new ONode().set("code", 403).set("msg", "你，没有权限哟!").toJson());
                });
    }
}

//登录示例
@Controller
public class LoginController{
    @Mapping("/login")
    public Result login(String userName, String userPassword){
        //尝试登录
        Subject subject = GritClient.global().auth().login(userName, userPassword);

        if (Subject.isEmpty(subject)) {
            return Result.failure("提示：账号或密码不对！");
        } else {
            //加载用户主体信息到会话状态
            Session.current().loadSubject(subject);
            
            //找到用户有权限的一个可见uri
            Resource res = GritClient.global().auth().getUriFrist(subject.subject_id);

            if (Utils.isEmpty(res.link_uri)) {
                //如果没找到
                return Result.failure("提示：请联系管理员开通权限！");
            } else {
                //如果找到，让前端到跳到目标地址
                String resUrl = GritUtil.buildDockUri(res);
                return Result.succeed(resUrl);
            }
        }
    }
}

//权限控制示例
@Controller
public class DemoController{
    //注解模式
    @AuthPermissions("demo:admin")
    public void demo_ajax_del(String key){
        //删除操作，有 demo:admin 权限的人才能操作
    }

    //代码模式
    public void demo_ajax_del2(String key){
        //删除操作，有 demo:admin 权限的人才能操作
        if(GritClient.global().auth().hasPermission(subject_id, "demo:admin")){
            return;
        }
    } 
}

//构建动态菜单的示例
@Component("view:header")
public class HeaderTag implements TemplateDirectiveModel{
    public void execute(){
        //获取用户有权限的可见uri分组
        List<ResourceGroup> groupList = GritClient.global().auth().getUriGroupList(subjectId);

        //获取分组的uri资源列表（内部是虚拟树）
        List<ResourceEntity> list = GritClient.global().auth().getUriListByGroup(subjectId, group.resource_id);
        
        //获取分组的第一个uri资源
        ResourceEntity frist = GritClient.global().auth().getUriFristByGroup(subjectId, group.resource_id);
    }
}

```

实战参考：[https://gitee.com/noear/water/tree/master/wateradmin](https://gitee.com/noear/water/tree/master/wateradmin)
