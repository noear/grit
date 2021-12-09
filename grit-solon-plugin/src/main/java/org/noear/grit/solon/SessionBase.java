package org.noear.grit.solon;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Subject;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;

/**
 * 跨应用会话状态
 *
 * @author noear
 * @since 1.0
 * */
public abstract class SessionBase extends SessionAbstractBase {
    public static final String GRIT_ADMIN_TOKEN = "grit_admin_token";
    private static SessionBase _global;

    /**
     * 会话全局实例（可能为null）
     */
    public static SessionBase global() {
        return _global;
    }

    public SessionBase() {
        if (_global == null) {
            _global = this;
        }
    }

    /**
     * 清除会话（但管理管理令牌）
     * */
    public void clear() {
        Context ctx = context();

        if (ctx != null) {
            //获取 admin token
            String adminToken = ctx.session(GRIT_ADMIN_TOKEN, "");

            //清除 session
            ctx.sessionClear();

            //将 admin token 存回去
            if (Utils.isNotEmpty(adminToken)) {
                ctx.sessionSet(GRIT_ADMIN_TOKEN, adminToken);
            }
        }
    }

    /**
     * 推送会话状态（起到附助作用）
     * */
    public void publish() {
        Context ctx = context();
        if (ctx != null) {
            ctx.sessionState().sessionPublish();
        }
    }


    /**
     * 加载用户数据模型
     */
    public void loadSubject(String loginName, String loginPassword) throws Exception {
        loadSubject(GritClient.global().auth().login(loginName, loginPassword));
    }

    /**
     * 加载用户数据模型
     */
    public abstract void loadSubject(Subject subject) throws Exception;


    /**
     * 获取用户Id
     */
    public final long getSubjectId() {
        long temp = globalSubjectId();

        if (temp > 0 && (temp != localSubjectId())) {
            try {
                Subject subject = GritClient.global().subject().getSubjectById(temp);
                loadSubject(subject);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return temp;
    }

    /**
     * 设置用户Id
     */
    public final void setSubjectId(long subjectId) {
        globalSet(SUBJECT_ID, subjectId);
        localSet(SUBJECT_ID, subjectId);
    }

    /**
     * 获取登录名
     */
    public final String getLoginName() {
        return globalGet("loginName", null);
    }

    /**
     * 设置登录名
     */
    public final void setLoginName(String loginName) {
        globalSet("loginName", loginName);
    }

    /**
     * 获取显示名
     */
    public final String getDisplayName() {
        return globalGet("displayName", null);
    }

    /**
     * 设置显示名
     */
    public final void setDisplayName(String displayName) {
        globalSet("displayName", displayName);
    }
}