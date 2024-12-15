package org.noear.grit.server.api.integration;

import org.noear.grit.server.GritServerConfig;
import org.noear.grit.server.api.service.*;
import org.noear.solon.Utils;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Plugin;

/**
 * @author noear
 * @since 1.0
 */
public class GritServerPlugin implements Plugin {
    @Override
    public void start(AppContext context) {
        context.beanScan(GritServerConfig.class);
        GritServerConfig serverConfig = context.getBean(GritServerConfig.class);

        if (Utils.isNotEmpty(serverConfig.gritApiToken())) {
            routerAdd(context, AuthServiceImpl.class);
            routerAdd(context, ResourceLinkServiceImpl.class);
            routerAdd(context, ResourceSchemaServiceImpl.class);
            routerAdd(context, ResourceServiceImpl.class);
            routerAdd(context, SubjectLinkServiceImpl.class);
            routerAdd(context, SubjectServiceImpl.class);
        }

        GritServerUpdate.tryUpdate();
    }

    private void routerAdd(AppContext context, Class<?> clz) {
        BeanWrap bw = context.getWrap(clz);
        bw.remotingSet(true);

        context.app().router().add(bw);
    }
}