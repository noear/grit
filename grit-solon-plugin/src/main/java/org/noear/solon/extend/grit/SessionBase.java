package org.noear.solon.extend.grit;

import org.noear.grit.client.StoneClient;
import org.noear.grit.client.model.User;

/**
 * 跨应用会话状态
 *
 * @author noear
 * @since 1.0
 * */
public abstract class SessionBase extends SessionAbstractBase {
    private static SessionBase _global;

    /**
     * 会话全局实例（可能为null）
     * */
    public static SessionBase global() {
        return _global;
    }

    public SessionBase() {
        if (_global == null) {
            _global = this;
        }
    }


    /**
     * 加载用户数据模型
     * */
    public void loadModel(String userID, String password) throws Exception {
        loadModel(StoneClient.login(userID, password));
    }

    /**
     * 加载用户数据模型
     * */
    public abstract void loadModel(User model) throws Exception;


    /**
     * 获取用户Id
     */
    public final long getUserId() {
        long temp = globalUserId();

        if (temp > 0 && (temp != localUserId())) {
            try {
                User user = StoneClient.user().getUserById(temp);
                loadModel(user);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return temp;
    }

    /**
     * 设置用户Id
     */
    public final void setUserId(long userId) {
        globalSet("userId", String.valueOf(userId));
        localSet("userId", String.valueOf(userId));
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