package gritdock;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.grit.client.GritClient;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.memcached.MemCache;

/**
 * @author noear
 * @since 1.0
 * */
@Configuration
public class Config {
    private static ICacheServiceEx cache;
    private static DbContext db;

    public static String evn() {
        return Solon.cfg().get("stonedock.env", "");
    }

    public static String title() {
        return Solon.cfg().get("stonedock.title", Solon.cfg().appTitle());
    }

    public static DbContext db() {
        return db;
    }

    public static ICacheServiceEx cache() {
        return cache;
    }

    @Bean
    public void initStoneClient() {
        cache = new MemCache(Solon.cfg().getProp("stone.cache"));
        db = new DbContext(Solon.cfg().getProp("stone.db"));

        GritClient.init(db, cache);
    }
}
