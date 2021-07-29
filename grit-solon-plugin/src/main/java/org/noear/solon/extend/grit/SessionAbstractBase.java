package org.noear.solon.extend.grit;

import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;

/**
 * @author noear
 * @since 1.0
 */
public abstract class SessionAbstractBase {
    protected Context context() {
        return Context.current();
    }


    /////////////////////////////////////////////////
    //
    // 全局属性
    //

    protected long globalUserId() {
        return Long.parseLong(globalGet("userId", "0"));
    }

    /**
     * 获取全局会话变量
     */
    protected String globalGet(String key, String def) {
        Object tmp = context().session(key);
        if (tmp == null) {
            return def;
        } else {
            return tmp.toString();
        }
    }

    /**
     * 设置全局会话变量
     */
    protected void globalSet(String key, String val) {
        context().sessionSet(key, val);
    }

    /**
     * 检测全局会话变量
     */
    protected boolean globalHas(String key) {
        return context().session(key) != null;
    }


    /////////////////////////////////////////////////
    //
    // 本地应用属性
    //

    /**
     * 本地应用用户Id
     */
    protected long localUserId() {
        return Long.parseLong(localGet("userId", "0"));
    }

    /**
     * 获取本地应用会话变量
     */
    public String localGet(String key, String def) {
        return globalGet(Solon.cfg().appName() + "_" + key, def);
    }


    /**
     * 设置本地应用会话变量
     */
    public void localSet(String key, String val) {
        globalSet(Solon.cfg().appName() + "_" + key, val);
    }

    /**
     * 检测本地应用会话变量
     */
    public boolean localHas(String key) {
        return globalHas(Solon.cfg().appName() + "_" + key);
    }
}
