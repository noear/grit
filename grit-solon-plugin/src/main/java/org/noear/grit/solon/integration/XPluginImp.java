package org.noear.grit.solon.integration;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Config;
import org.noear.solon.core.Plugin;
import org.noear.grit.client.GritClient;
import org.noear.solon.core.event.AppLoadEndEvent;

/**
 * Grit 集成插件
 *
 * @author noear
 * @since 1.0
 */
public class XPluginImp implements Plugin {
    static final String GRIT_INIT_CONFIG = "grit-init.jsond";

    @Override
    public void start(SolonApp app) {
        app.onEvent(AppLoadEndEvent.class, e -> {
            String appName = Solon.cfg().appName();

            if (appName != null && "gritdock".equals(appName) == false) {
                //0.初始化架构
                initGritSpace(appName);

                //1.初始化资源空间
                GritClient.global().setCurrentSpaceByCode(appName);
            }

            //2.加载domain.js
            app.get("/_session/domain.js", (ctx) -> {
                String domain = app.cfg().get("server.session.state.domain");
                if (Utils.isEmpty(domain) == false) {
                    if (ctx.uri().getHost().indexOf(domain) >= 0) {
                        ctx.contentType("text/javascript");
                        ctx.output("try { document.domain = '" + domain + "'; }catch (err) { }");
                    }
                }
            });
        });
    }

    private void initGritSpace(String appName) throws Exception {
        String jsond = Utils.getResourceAsString(GRIT_INIT_CONFIG);

        if (Utils.isEmpty(jsond)) {
            return;
        }

        //
        //借用配置服务，做初始化版本管理
        //

        String jsond_md5 = Utils.md5(jsond);

        if (GritClient.global().resource().hasSpaceByCode(appName)) {
            //如果存在资源空间（则比较初始化文件的哈希码）
            String jsond_md5C = null;

            Config config = CloudClient.config().pull("_grit", appName);

            if (config != null) {
                jsond_md5C = config.value();
            }

            if (jsond_md5.equals(jsond_md5C)) {
                return;
            }
        }

        GritClient.global().resourceSchema().importSchema(jsond);
        CloudClient.config().push("_grit", appName, jsond_md5);
    }
}
