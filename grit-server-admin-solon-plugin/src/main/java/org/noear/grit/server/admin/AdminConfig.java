package org.noear.grit.server.admin;

import org.noear.grit.server.GritServerConfig;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;

/**
 * @author noear
 * @since 1.0
 * */
@Configuration
public class AdminConfig {
    @Bean
    public void init() {
        GritServerConfig.staticPrefix("/grit");
    }

    /**
     * 平台标题
     */
    public static String title() {
        return Solon.cfg().get("gritadmin.title", "Grit - 控制台");
    }
}
