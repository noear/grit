package org.noear.grit.server.admin;

import org.noear.solon.Solon;

/**
 * @author noear
 * @since 1.0
 * */
public class Config {


    public static boolean enable() {
        return Solon.cfg().getBool("gritdock.enable", true);
    }

    /**
     * 平台环境
     */
    public static String env() {
        return Solon.cfg().get("gritdock.env", "");
    }

    /**
     * 平台标题
     */
    public static String title() {
        return Solon.cfg().get("gritdock.title", Solon.cfg().appTitle());
    }
}
