package gritadmin.dso;


import org.noear.grit.client.GritClient;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.extend.health.HealthHandler;

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
            chain.doFilter(ctx);
        }
    }

    private boolean authCheck(Context ctx) {
        String path = ctx.path();

        //允许接口跳过
        if (path.startsWith(GritClient.getRpcPath())) {
            return true;
        }

        //特殊的路径通过
        if (path.startsWith("/grit")) {
            //有会话账号能过
            if (Utils.isNotEmpty(ctx.session("user", ""))) {
                return true;
            }

            //否则，到登录页去
            ctx.setHandled(true);
            ctx.redirect("/login");
            return false;
        } else {
            return true;
        }
    }
}