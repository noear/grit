package org.noear.solon.extend.grit;

import org.noear.solon.core.handle.Context;
import org.noear.grit.client.StoneClient;

public abstract class InterceptorBase {
    /**
     * 用户ID
     */
    public long getUserId() {
        return SessionBase.global().getUserId();
    }

    /**
     * 验证代理::true:通过,false未通过（可以重写）
     */
    public void verifyHandle(Context ctx) throws Exception {
        String path = ctx.path().toLowerCase();
        long userId = getUserId();

        if (userId > 0) {
            String userDisplayName = SessionBase.global().getDisplayName();

            //old
            ctx.attrSet("user_puid", String.valueOf(userId));
            ctx.attrSet("user_name", userDisplayName);
            //new
            ctx.attrSet("user_id", String.valueOf(userId));
            ctx.attrSet("user_display_name", userDisplayName);
        }

        if (path.indexOf("/ajax/") < 0 && path.startsWith("/login") == false) {
            if (StoneClient.resource().hasResourcePath(path)) {

                if (userId == 0) {
                    ctx.redirect("/login");
                    ctx.setHandled(true);
                    return;
                }

                if (StoneClient.userHasPath(userId, path) == false) {
                    ctx.outputAsHtml("Sorry, no permission!");
                    ctx.setHandled(true);
                    return;
                }
            }
        }
    }
}
