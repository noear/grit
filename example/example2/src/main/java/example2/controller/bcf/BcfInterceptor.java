package example2.controller.bcf;

import org.noear.bcf.BcfInterceptorBase;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.srww.uadmin.dso.Session;
import org.noear.water.WaterClient;

/**
 * @author noear 2021/2/16 created
 */
@Controller
public class BcfInterceptor extends BcfInterceptorBase {
    @Override
    public int getPUID() {
        return Session.current().getPUID();
    }

    @Override
    @Mapping(value = "**", before = true)
    public void verifyHandle(Context ctx) throws Exception {
        String path = ctx.pathNew().toLowerCase();

        if (path.equals("/login")) {
            return;
        }

        if (path.contains("/uncheck/")) {
            return;
        }

        if (Solon.cfg().isWhiteMode()) {
            if (ctx.uri().getHost().indexOf("localhost") < 0) {
                String ip = ctx.realIp();
                if (WaterClient.Whitelist.existsOfClientAndServerIp(ip) == false) {
                    ctx.output(ip + ",not is whitelist!");
                    ctx.setHandled(true);
                    return;
                }
            }
        }

        super.verifyHandle(ctx);
    }
}
