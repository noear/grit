package org.noear.solon.extend.grit.integration;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
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
    @Override
    public void start(SolonApp app) {
        app.onEvent(AppLoadEndEvent.class, e -> {
            String appName = Solon.cfg().appName();

            //1.初始化资源空间
            if (appName != null) {
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
}
