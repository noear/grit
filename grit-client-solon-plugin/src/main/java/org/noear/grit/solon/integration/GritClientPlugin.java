package org.noear.grit.solon.integration;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Config;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.grit.client.GritClient;
import org.noear.solon.core.event.AppLoadEndEvent;
import org.noear.solon.core.util.ResourceUtil;
import org.noear.solonx.licence.LicenceInfo;

/**
 * Grit 集成插件
 *
 * @author noear
 * @since 1.0
 */
public class GritClientPlugin implements Plugin {
    static final String GRIT_INIT_CONFIG = "grit-init.jsond";

    @Override
    public void start(AppContext context) {
        //避免补排除
        LicenceInfo.check();

        Solon.app().onEvent(AppLoadEndEvent.class, e -> {
            String appName = Solon.cfg().appName();

            if (appName != null && "gritdock".equals(appName) == false) {
                //0.初始化架构
                initGritSpace(appName);

                //1.初始化资源空间
                GritClient.global().setCurrentSpaceByCode(appName);
            }

            //2.加载domain.js
            Solon.app().get("/_session/domain.js", (ctx) -> {
                String domain = Solon.cfg().get("server.session.state.domain");
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
        String data = ResourceUtil.getResourceAsString(GRIT_INIT_CONFIG);

        if (Utils.isEmpty(data)) {
            return;
        }

        //
        //借用配置服务，做初始化版本管理
        //

        String data_md5 = Utils.md5(data);

        if (GritClient.global().resource().hasSpaceByCode(appName)) {
            //如果存在资源空间（则比较初始化文件的哈希码）
            String data_md5C = null;

            Config config = CloudClient.config().pull("_grit", appName);

            if (config != null) {
                data_md5C = config.value();
            }

            if (data_md5.equals(data_md5C)) {
                return;
            }
        }


        GritClient.global().resourceSchema().importSchema(data);
        CloudClient.config().push("_grit", appName, data_md5);
    }
}
