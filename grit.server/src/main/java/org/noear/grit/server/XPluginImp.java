package org.noear.grit.server;

import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;

/**
 * @author noear
 * @since 1.0
 */
public class XPluginImp implements Plugin {
    @Override
    public void start(AopContext context) {
        context.beanScan(XPluginImp.class);

        GritUpdate.tryUpdate();
    }
}
