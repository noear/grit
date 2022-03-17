package org.noear.grit.server;

import org.noear.solon.SolonApp;
import org.noear.solon.core.Plugin;

/**
 * @author noear
 * @since 1.0
 */
public class XPluginImp implements Plugin {
    @Override
    public void start(SolonApp app) {
        app.beanScan(XPluginImp.class);

        GritUpdate.tryUpdate();
    }
}
