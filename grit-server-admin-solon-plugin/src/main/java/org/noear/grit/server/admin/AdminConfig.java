package org.noear.grit.server.admin;

import org.noear.solon.Solon;

/**
 * @author noear
 * @since 1.0
 * */
public class AdminConfig {
    /**
     * 平台标题
     */
    public static String title() {
        return Solon.cfg().get("gritadmin.title", "Grit - 控制台");
    }
}
