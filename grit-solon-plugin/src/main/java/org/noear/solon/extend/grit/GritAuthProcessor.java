package org.noear.solon.extend.grit;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritException;
import org.noear.solon.Solon;
import org.noear.solon.auth.AuthProcessor;
import org.noear.solon.auth.annotation.Logical;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.handle.Context;

import java.sql.SQLException;

/**
 * 授权处理实现
 *
 * @author noear
 * @since 1.0
 */
public class GritAuthProcessor implements AuthProcessor {
    protected long getUserId(){
        return SessionBase.global().getUserId();
    }

    /**
     * 用户显示名
     * */
    protected String getUserDisplayName() {
        return SessionBase.global().getDisplayName();
    }

    @Override
    public boolean verifyIp(String ip) {
        //安装模式，则忽略
        if (Solon.cfg().isSetupMode()) {
            return true;
        }

        //非白名单模式，则忽略
        if (Solon.cfg().isWhiteMode() == false) {
            return true;
        }

        boolean isOk = CloudClient.list().inListOfClientAndServerIp(ip);

        long userId = getUserId();

        if (userId > 0) {
            String userDisplayName = getUserDisplayName();
            Context ctx = Context.current();

            //old
            ctx.attrSet("user_puid", String.valueOf(userId));
            ctx.attrSet("user_name", userDisplayName);
            //new
            ctx.attrSet("user_id", String.valueOf(userId));
            ctx.attrSet("user_display_name", userDisplayName);
        }

        return isOk;
    }

    @Override
    public boolean verifyLogined() {
        //安装模式，则忽略
        if (Solon.cfg().isSetupMode()) {
            return true;
        }

        return getUserId() > 0;
    }

    @Override
    public boolean verifyPath(String path, String method) {
        //安装模式，则忽略
        if (Solon.cfg().isSetupMode()) {
            return true;
        }

        try {
            if (GritClient.resource().hasResourcePath(path)) {
                return true;
            } else {
                return GritClient.userHasPath(getUserId(), path);
            }
        } catch (SQLException e) {
            throw new GritException(e);
        }
    }

    @Override
    public boolean verifyPermissions(String[] permissions, Logical logical) {
        //安装模式，则忽略
        if (Solon.cfg().isSetupMode()) {
            return true;
        }

        long userId = getUserId();

        try {
            if (logical == Logical.AND) {
                boolean isOk = true;

                for (String p : permissions) {
                    isOk = isOk && GritClient.userHasPermission(userId, p);
                }

                return isOk;
            } else {
                for (String p : permissions) {
                    if (GritClient.userHasPermission(userId, p)) {
                        return true;
                    }
                }
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifyRoles(String[] roles, Logical logical) {
        //安装模式，则忽略
        if (Solon.cfg().isSetupMode()) {
            return true;
        }

        long userId = getUserId();

        try {
            if (logical == Logical.AND) {
                boolean isOk = true;

                for (String p : roles) {
                    isOk = isOk && GritClient.userHasRole(userId, p);
                }

                return isOk;
            } else {
                for (String p : roles) {
                    if (GritClient.userHasRole(userId, p)) {
                        return true;
                    }
                }
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
