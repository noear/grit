package gritdock;

import org.noear.solon.Solon;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

/**
 * @author noear
 * @since 1.0
 * */
public class Config {
    private static ICacheServiceEx cache = new LocalCache();

    /**
     * 平台环境
     * */
    public static String evn() {
        return Solon.cfg().get("gritdock.env", "");
    }

    /**
     * 平台标题
     * */
    public static String title() {
        return Solon.cfg().get("gritdock.title", Solon.cfg().appTitle());
    }


    /**
     * 缓存服务
     * */
    public static ICacheServiceEx cache() {
        return cache;
    }
}
