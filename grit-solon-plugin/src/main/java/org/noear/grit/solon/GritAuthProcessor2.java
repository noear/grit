package org.noear.grit.solon;

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
public class GritAuthProcessor2 extends AuthProcessorBase {
    /**
     * 获取主体Id
     * */
    protected long getSubjectId() {
        return SessionBase.global().getSubjectId();
    }

    /**
     * 获取主体显示名
     */
    protected String getSubjectDisplayName() {
        return SessionBase.global().getDisplayName();
    }

    @Override
    public boolean verifyIp(String ip) {
        //安装模式，则忽略
        if (Solon.cfg().isSetupMode()) {
            return true;
        }

        long subjectId = getSubjectId();

        if (subjectId > 0) {
            String subjectDisplayName = getSubjectDisplayName();
            Context ctx = Context.current();

            if (ctx != null) {
                //old
                ctx.attrSet("user_puid", String.valueOf(subjectId));
                ctx.attrSet("user_name", subjectDisplayName);
                //new
                ctx.attrSet("user_id", String.valueOf(subjectId));
                ctx.attrSet("user_display_name", subjectDisplayName);
            }
        }

        //非白名单模式，则忽略
        if (Solon.cfg().isWhiteMode() == false) {
            return true;
        }

        return CloudClient.list().inListOfClientAndServerIp(ip);
    }

    @Override
    public boolean verifyLogined() {
        //安装模式，则忽略
        if (Solon.cfg().isSetupMode()) {
            return true;
        }

        return getSubjectId() > 0;
    }

    @Override
    public boolean verifyPath(String path, String method) {
        //安装模式，则忽略
        if (Solon.cfg().isSetupMode()) {
            return true;
        }

        try {
            if (GritClient.global().resource().hasResourceByUri(path) == false) {
                return true;
            } else {
                return GritClient.global().auth().hasUri(getSubjectId(), path);
            }
        } catch (SQLException e) {
            throw new GritException(e);
        }
    }

    @Override
    protected List<String> getPermissions() {
        List<String> permissionList = null;
        Context ctx = Context.current();

        String sessionKey = GritClient.global().getCurrentSpaceCode() + ":" + "user_permissionList";

        //尝试从会话状态获取
        if (ctx != null) {
            permissionList = ctx.session(sessionKey, null);
        }

        if (permissionList == null) {
            try {
                permissionList = GritClient.global().auth()
                        .getPermissionList(getSubjectId())
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

        String sessionKey = GritClient.global().getCurrentSpaceCode() + ":" + "user_roleList";

        //尝试从会话状态获取
        if (ctx != null) {
            roleList = ctx.session(sessionKey, null);
        }

        if (roleList == null) {
            try {
                roleList = GritClient.global().auth()
                        .getRoleList(getSubjectId())
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
