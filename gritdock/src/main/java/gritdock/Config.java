package gritdock;

import org.noear.solon.Solon;

/**
 * @author noear
 * @since 1.0
 * */
public class Config {

    public static final String grit_version = "v1.8.1";

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
