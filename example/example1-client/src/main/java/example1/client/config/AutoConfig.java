package example1.client.config;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.auth.AuthAdapter;

/**
 * @author noear 2021/6/1 created
 */
@Configuration
public class AutoConfig {
    @Bean
    public AuthAdapter init() {
        //
        // 构建适配器
        //
        return new AuthAdapter()
                .loginUrl("/login") //设定登录地址，未登录时自动跳转
                .addRule(r -> r.include("**").verifyIp().failure((c, t) -> c.output("你的IP不在白名单"))) //添加规则
                .addRule(b -> b.exclude("/login**").exclude("/_**").verifyPath()) //添加规则
                .processor(new AuthProcessorImpl()); //设定认证处理器
    }
}
