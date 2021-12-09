package gritdock.controller;

import gritdock.dso.Session;
import org.noear.grit.client.GritClient;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.core.handle.Result;

/**
 * 应用过滤器，增加访问控制
 *
 * @author noear
 * @since 1.0
 */
@Component
public class AppFilterImpl implements Filter {
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        if (authCheck(ctx)) {
            try {
                chain.doFilter(ctx);
            } catch (Throwable e) {
                EventBus.push(e);
                ctx.render(Result.failure(e.getLocalizedMessage()));
            }
        }
    }

    private boolean authCheck(Context ctx) {
        String path = ctx.path();

        //允许接口跳过
        if (path.startsWith(GritClient.RPC_PATH)) {
            return true;
        }

        if (path.startsWith("/grit/login") ||
                path.startsWith("/login") ||
                path.startsWith("/_") ||
                path.endsWith(".ico")) {
            return true;
        }

        //for white model
        if (Solon.cfg().isWhiteMode()) {
            String ip = ctx.realIp();

            if (Solon.cfg().isFilesMode() == false) {
                if (CloudClient.list().inListOfClientAndServerIp(ip) == false) {
                    ctx.setHandled(true);
                    ctx.output(ip + ", not is whitelist!");
                    return false;
                }
            }
        }

        //for grit admin
        if (path.startsWith("/grit/")) {
            //有会话账号能过
            String user0 = Solon.cfg().get("gritadmin.user", "admin");
            String password0 = Solon.cfg().get("gritadmin.password", "");
            String admin_token = Utils.md5(user0 + "#" + password0);

            if (admin_token.equals(ctx.session(Session.GRIT_ADMIN_TOKEN, ""))) {
                return true;
            }

            //否则，到登录页去
            ctx.setHandled(true);
            ctx.redirect("/grit/login");
            return false;
        }

        //for dock
        if (Session.current().getSubjectId() == 0) {
            ctx.setHandled(true);
            ctx.redirect("/login");
            return false;
        }

        return true;
    }
}