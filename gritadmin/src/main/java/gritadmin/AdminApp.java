package gritadmin;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Import;

import java.util.Properties;

/**
 * @author noear 2021/6/24 created
 */
public class AdminApp {
    public static void main(String[] args) {
        Solon.start(AdminApp.class, args, app -> {
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
            Solon.cfg().remove("solon.cloud.water.server");
        }
    }
}
