package org.noear.grit.solon;

import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;

/**
 * @author noear
 * @since 1.0
 */
public abstract class SessionAbstractBase {
    protected static final String SUBJECT_ID = "subjectId";

    protected Context context() {
        return Context.current();
    }


    /////////////////////////////////////////////////
    //
    // 全局属性
    //

    protected long globalSubjectId() {
        return globalGetAsLong(SUBJECT_ID, 0L);
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
     * 获取全局会话变量，并转为Long
     */
    protected long globalGetAsLong(String key, long def) {
        String tmp = globalGet(key, String.valueOf(def));
        return Long.parseLong(tmp);
    }

    /**
     * 获取全局会话变量，并转为Int
     */
    protected int globalGetAsInt(String key, int def) {
        String tmp = globalGet(key, String.valueOf(def));
        return Integer.parseInt(tmp);
    }

    /**
     * 设置全局会话变量
     */
    protected void globalSet(String key, Object val) {
        if(val == null){
            val = "";
        }

        context().sessionSet(key, val.toString());
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
    protected long localSubjectId() {
        return localGetAsLong(SUBJECT_ID, 0L);
    }

    /**
     * 获取本地应用会话变量
     */
    public String localGet(String key, String def) {
        return globalGet(Solon.cfg().appName() + "_" + key, def);
    }

    /**
     * 获取本地应用会话变量，并转为Long
     */
    public long localGetAsLong(String key, long def){
        String tmp = localGet(key, String.valueOf(def));
        return Long.parseLong(tmp);
    }

    /**
     * 获取本地应用会话变量，并转为Int
     */
    public int localGetAsInt(String key, int def){
        String tmp = localGet(key, String.valueOf(def));
        return Integer.parseInt(tmp);
    }


    /**
     * 设置本地应用会话变量
     */
    public void localSet(String key, Object val) {
        if (val == null) {
            val = "";
        }

        globalSet(Solon.cfg().appName() + "_" + key, val.toString());
    }

    /**
     * 检测本地应用会话变量
     */
    public boolean localHas(String key) {
        return globalHas(Solon.cfg().appName() + "_" + key);
    }
}
