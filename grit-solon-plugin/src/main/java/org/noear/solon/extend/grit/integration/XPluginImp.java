package org.noear.solon.extend.grit.integration;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.core.Plugin;
import org.noear.grit.client.GritClient;
import org.noear.weed.DbContext;
import org.noear.weed.cache.memcached.MemCache;

import java.util.Properties;

/**
 * Stone 集成插件
 *
 * @author noear
 * @since 1.0
 */
public class XPluginImp implements Plugin {
    @Override
    public void start(SolonApp app) {
        String p_root = Solon.cfg().appName();
        Properties p_cache = app.cfg().getProp("stone.cache");
        Properties p_db = app.cfg().getProp("stone.db");

        //1.初始化
        if (p_cache.size() > 1 && p_db.size() > 1 && p_root != null) {

            MemCache cache = new MemCache(p_cache);
            DbContext db = getDbDo(p_db);

            GritClient.init(db, cache);
            GritClient.branched(p_root);
        }

        //2.加载domain.js
        app.get("/_session/domain.js", (ctx) -> {
            String domain = app.cfg().get("server.session.state.domain");
            if (Utils.isEmpty(domain) == false) {
                if (ctx.uri().getHost().indexOf(domain) >= 0) {
                    ctx.contentType("text/javascript");
                    ctx.output("try { document.domain = '" + domain + "'; }catch (err) { }");
                }
            }
        });
    }

    private DbContext getDbDo(Properties prop) {
        HikariDataSource source = new HikariDataSource();
        Utils.injectProperties(source, prop);

        return new DbContext(source);
    }
}
