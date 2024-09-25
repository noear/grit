package org.noear.grit.server.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.slf4j.MDC;

/**
 * 后置处理（用于记录日志）
 *
 * @author noear
 * @since 1.0
 */
@Slf4j
public class AfterHandler implements Filter {

    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        chain.doFilter(ctx);

        Long time_start = ctx.attr("time_start");
        StringBuilder buf = new StringBuilder();
        buf.append("> Header: ").append(ctx.headerMap()).append("\n");
        buf.append("> Param: ").append(ctx.paramMap()).append("\n");
        buf.append("> Body: ").append(ctx.body()).append("\n");
        if (time_start != null) {
            buf.append("T Elapsed time: ")
                    .append(System.currentTimeMillis() - time_start).append("ms")
                    .append("\n");
        }

        buf.append("\n");

        MDC.put("tag0", "gritapi");
        MDC.put("tag1", ctx.path());

        if (ctx.errors == null) {
            buf.append("< Body: ").append(ONode.stringify(ctx.result));
            log.info("{}", buf);
        } else {
            buf.append("< Error: ").append(Utils.throwableToString(ctx.errors));
            log.error("{}", buf);
        }
    }
}
