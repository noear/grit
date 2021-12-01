package gritapi;

import org.noear.solon.Solon;

import java.util.Properties;

/**
 * @author noear
 * @since 1.0
 */
public class ApiApp {
    public static void main(String[] args) {
        Solon.start(ApiApp.class, args, app -> {
            app.cfg().loadEnv("grit");
            app.onError(e -> e.printStackTrace());

            //仅在初始化时有效
            dbSwitchTry();
        });
    }

    /**
     * 数据配置切换
     */
    private static void dbSwitchTry() {
        Properties props = Solon.cfg().getProp("grit.db");
        if (props.size() > 0) {
            //如果有 grit.db 的配置，则移除 water 配置中心
            //
            System.getProperties().remove("solon.cloud.water.server");
            Solon.cfg().remove("solon.cloud.water.server");
        }
    }
}
