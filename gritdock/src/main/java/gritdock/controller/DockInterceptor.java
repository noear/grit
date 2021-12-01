package gritdock.controller;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import gritdock.dso.Session;

@Mapping(value = "**", before = true)
@Component
public class DockInterceptor implements Handler {

    @Override
    public void handle(Context ctx) throws Throwable {
        //IP白名单校验
        String path = ctx.path();

        if (ctx.uri().getHost().indexOf("localhost") < 0) {
            String ip = ctx.realIp();

            if (Solon.cfg().isWhiteMode() && Solon.cfg().isFilesMode() == false) {
                if (CloudClient.list().inListOfClientAndServerIp(ip) == false) {
                    ctx.setHandled(true);
                    ctx.output(ip + ",not is whitelist!");
                    return;
                }
            }
        }

        if (path.indexOf("/ajax/") >= 0 ||
                path.startsWith("/login") ||
                path.startsWith("/run/") ||
                path.startsWith("/msg/") ||
                path.startsWith("/_")) {
            return;
        }

        if (Session.current().getSubjectId() == 0) {
            ctx.setHandled(true);
            ctx.redirect("/login");
        }
    }
}