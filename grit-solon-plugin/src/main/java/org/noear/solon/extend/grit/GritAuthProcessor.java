package org.noear.solon.extend.grit;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritException;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.auth.AuthProcessorBase;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.handle.Context;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 授权处理实现
 *
 * @author noear
 * @since 1.0
 */
public class GritAuthProcessor extends AuthProcessorBase {
    protected long getUserId() {
        return SessionBase.global().getUserId();
    }

    /**
     * 用户显示名
     */
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
            if (GritClient.resource().hasResourceUri(path)) {
                return true;
            } else {
                return GritClient.auth().subjectHasUri(getUserId(), path);
            }
        } catch (SQLException e) {
            throw new GritException(e);
        }
    }

    @Override
    protected List<String> getPermissions() {
        List<String> permissionList = null;
        Context ctx = Context.current();

        String sessionKey = GritClient.currentResourceSpaceCode() + ":" + "user_permissionList";

        //尝试从会话状态获取
        if (ctx != null) {
            permissionList = ctx.session(sessionKey, null);
        }

        if (permissionList == null) {
            try {
                permissionList = GritClient.auth()
                        .getSubjectPermissionList(getUserId())
                        .stream()
                        .filter(s -> Utils.isNotEmpty(s.resource_code))
                        .map(s -> s.resource_code)
                        .collect(Collectors.toList());

                //尝试设置到会话状态
                if (ctx != null) {
                    ctx.sessionSet(sessionKey, permissionList);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return permissionList;
    }

    @Override
    protected List<String> getRoles() {
        List<String> roleList = null;
        Context ctx = Context.current();

        String sessionKey = GritClient.currentResourceSpaceCode() + ":" + "user_roleList";

        //尝试从会话状态获取
        if (ctx != null) {
            roleList = ctx.session(sessionKey, null);
        }

        if (roleList == null) {
            try {
                roleList = GritClient.auth()
                        .getSubjectRoleList(getUserId())
                        .stream()
                        .filter(s -> Utils.isNotEmpty(s.subject_code))
                        .map(s -> s.subject_code)
                        .collect(Collectors.toList());

                //尝试设置到会话状态
                if (ctx != null) {
                    ctx.sessionSet(sessionKey, roleList);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return roleList;
    }
}
