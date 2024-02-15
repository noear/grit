package org.noear.grit.server.dso;


import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.slf4j.MDC;

/**
 * 前置处理
 *
 * @author noear
 * @since 1.0
 */
@Component
public class BeforeHandler implements Handler {
    @Inject("${grit.token:}")
    String gritToken = "";

    @Override
    public void handle(Context ctx) throws Throwable {
        if (gritToken.equals(ctx.header("Grit-Token")) == false) {
            //
            //如果令牌不相同
            //
            MDC.put("tag0", "gritapi");
            throw new IllegalArgumentException("Invalid header: Grit-Token");
        }

        ctx.attrSet("time_start", System.currentTimeMillis());
    }
}
