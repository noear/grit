package org.noear.grit.server.api.dso;

import org.noear.grit.server.GritServerConfig;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.slf4j.MDC;

/**
 * 前置处理
 *
 * @author noear
 * @since 1.0
 */
public class BeforeHandler implements Filter {
    @Inject
    GritServerConfig serverConfig;


    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        if (serverConfig.gritApiToken().equals(ctx.header("Grit-Token")) == false) {
            //
            //如果令牌不相同
            //
            MDC.put("tag0", "gritapi");
            throw new IllegalArgumentException("Invalid header: Grit-Token");
        }

        ctx.attrSet("time_start", System.currentTimeMillis());

        chain.doFilter(ctx);
    }
}
