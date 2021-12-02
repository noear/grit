package gritdock;

import org.noear.solon.Solon;

/**
 * @author noear
 * @since 1.0
 * */
public class Config {
    public static String GRIT_ADMIN_TOKEN = "grit_admin_token";

    /**
     * 平台环境
     */
    public static String evn() {
        return Solon.cfg().get("gritdock.env", "");
    }

    /**
     * 平台标题
     */
    public static String title() {
        return Solon.cfg().get("gritdock.title", Solon.cfg().appTitle());
    }
}
