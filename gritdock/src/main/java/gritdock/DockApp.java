package gritdock;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.core.event.EventBus;

import java.util.Properties;

/**
 * @author noear
 * @since 1.0
 */
@SolonMain
public class DockApp {
    public static void main(String[] args) {
        Solon.start(DockApp.class, args, app -> {
            app.cfg().loadEnv("grit");

            //仅在初始化时有效
            gritDbSwitchTry();
        });

        //尝试注册 gritapi 服务
        gritApiRegTry();
    }

    /**
     * 尝试数据配置切换
     */
    private static void gritDbSwitchTry() {
        Properties props = Solon.cfg().getProp("grit.db");
        if (props.size() > 0) {
            //如果有 grit.db 的配置，则移除 water 配置中心
            //
            System.getProperties().remove("solon.cloud.water.server");
            Solon.cfg().remove("solon.cloud.water.server");
        }
    }

    /**
     * 尝试注册 gritapi 服务
     */
    private static void gritApiRegTry() {
        try {
            if (CloudClient.discovery() != null) {
                Instance instance = new Instance("gritapi",
                        Instance.local().host(),
                        Instance.local().port()).protocol("http");
                CloudClient.discovery().register("grit", instance);
            }
        } catch (Exception e) {
            EventBus.publish(e);
        }
    }
}
