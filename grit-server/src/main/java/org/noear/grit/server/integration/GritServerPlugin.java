package org.noear.grit.server.integration;

import org.noear.grit.server.GritServerConfig;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;

/**
 * @author noear
 * @since 1.0
 */
public class GritServerPlugin implements Plugin {
    @Override
    public void start(AppContext context) {
        context.beanScan(GritServerConfig.class);

        GritServerUpdate.tryUpdate();
    }
}
