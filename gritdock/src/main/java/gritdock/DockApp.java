package gritdock;

import org.noear.solon.Solon;
import org.noear.solon.Utils;

import java.util.Properties;

/**
 * @author noear
 * @since 1.0
 */
public class DockApp {
    public static void main(String[] args) {
        Solon.start(DockApp.class, args, app -> {
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
            return;
        }

        String server = Solon.cfg().get("grit.server");
        if(Utils.isNotEmpty(server)){
            //如果有 grit.server 的配置，则移除 water 配置中心；且采用 rpc 方式连接服务
            //
            System.getProperties().remove("solon.cloud.water.server");
            Solon.cfg().remove("solon.cloud.water.server");
            return;
        }
    }
}
