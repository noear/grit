package org.noear.grit.server.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.slf4j.MDC;

/**
 * 后置处理（用于记录日志）
 *
 * @author noear
 * @since 1.0
 */
@Slf4j
public class AfterHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Throwable {
        Long time_start = ctx.attr("time_start");
        StringBuilder buf = new StringBuilder();
        buf.append("> Params: ").append(ONode.stringify(ctx.paramsMap())).append("\n");
        if (time_start != null) {
            buf.append("T Elapsed time: ")
                    .append(System.currentTimeMillis() - time_start).append("ms")
                    .append("\n");
        }

        buf.append("\n");

        MDC.put("tag0", ctx.path());

        if (ctx.errors == null) {
            buf.append("< Body: ").append(ONode.stringify(ctx.result));
            log.info("{}", buf);
        } else {
            buf.append("< Error: ").append(Utils.throwableToString(ctx.errors));
            log.error("{}", buf);
        }
    }
}
