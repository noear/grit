package org.noear.grit.solon;

import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.noear.grit.client.GritClient;
import org.noear.solon.core.handle.Handler;

public abstract class GritPathInterceptorBase implements Handler {
    /**
     * 获取主体Id
     */
    protected long getSubjectId() {
        return SessionBase.global().getSubjectId();
    }

    /**
     * 获取主体显示名
     */
    protected String getSubjectDisplayName() {
        return SessionBase.global().getDisplayName();
    }

    /**
     * 验证代理::true:通过,false未通过（可以重写）
     */
    protected void verifyHandle(Context ctx) throws Exception {
        //安装模式，则忽略
        if (Solon.cfg().isSetupMode()) {
            return;
        }

        String path = ctx.path().toLowerCase();
        long subjectId = getSubjectId();

        if (subjectId > 0) {
            String subjectDisplayName = getSubjectDisplayName();

            //old
            ctx.attrSet("user_puid", String.valueOf(subjectId));
            ctx.attrSet("user_name", subjectDisplayName);
            //new
            ctx.attrSet("user_id", String.valueOf(subjectId));
            ctx.attrSet("user_display_name", subjectDisplayName);
        }

        if (path.indexOf("/ajax/") < 0 && path.startsWith("/login") == false) {
            if (GritClient.global().resource().hasResourceByUri(path)) {

                if (subjectId == 0L) {
                    ctx.redirect("/login");
                    ctx.setHandled(true);
                    return;
                }

                if (GritClient.global().auth().hasUri(subjectId, path) == false) {
                    ctx.outputAsHtml("Sorry, no permission!");
                    ctx.setHandled(true);
                    return;
                }
            }
        }
    }

    @Override
    public void handle(Context ctx) throws Throwable {
        verifyHandle(ctx);
    }
}
