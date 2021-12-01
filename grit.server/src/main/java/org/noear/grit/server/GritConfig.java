package org.noear.grit.server;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.noear.grit.client.GritClient;
import org.noear.grit.server.dso.CacheServiceWrap;
import org.noear.grit.server.dso.GritClientLocalImpl;
import org.noear.okldap.LdapClient;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.cache.CacheServiceSupplier;
import org.noear.solon.data.cache.CacheService;
import org.noear.solon.data.cache.LocalCacheService;
import org.noear.weed.DbContext;

import java.util.Properties;

/**
 * @author noear
 * @since 1.0
 */
@Configuration
public class GritConfig {
    static final String TML_MARK_SERVER = "${server}";
    static final String TML_MARK_SCHEMA = "${schema}";
    static final String TML_JDBC_URL = "jdbc:mysql://${server}/${schema}?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true";


    @Bean("grit.cache")
    public CacheService cache(@Inject("${grit.cache}") Properties props) {
        if (props.size() > 0) {
            CacheServiceSupplier supplier = new CacheServiceSupplier(props);
            return new CacheServiceWrap(supplier.get());
        } else {
            return new CacheServiceWrap(new LocalCacheService());
        }
    }

    @Bean("grit.db")
    public DbContext db(@Inject("${grit.db}") Properties props) {
        if (props.size() > 0) {
            String server = props.getProperty("server");
            String url = props.getProperty("url");

            if (Utils.isNotEmpty(server)) {
                props.remove("server");
                url = TML_JDBC_URL
                        .replace(TML_MARK_SERVER, server)
                        .replace(TML_MARK_SCHEMA, "grit");
            }

            if (Utils.isNotEmpty(url)) {
                props.remove("url");
                props.setProperty("jdbcUrl", url);
            }

            return new DbContext(new HikariDataSource(new HikariConfig(props)));
        } else {
            return null;
        }
    }

    @Bean
    public LdapClient ldapClient(@Inject("${grit.ldap}") Properties props) {
        if (props.size() > 0) {
            return new LdapClient(props);
        } else {
            return null;
        }
    }

    @Bean
    public GritClient gritClient(@Inject("${grit.db}") Properties props, @Inject GritClientLocalImpl clientLocal) {
        if (props.size() > 0) {
            GritClient.setGlobal(clientLocal);
            return clientLocal;
        } else {
            return null;
        }
    }
}
