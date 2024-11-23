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
import org.noear.solon.data.cache.CacheServiceSupplier;
import org.noear.solon.data.cache.CacheService;
import org.noear.solon.data.cache.LocalCacheService;
import org.noear.wood.DbContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author noear
 * @since 1.0
 */
@Configuration
public class GritConfig {
    static final Logger log = LoggerFactory.getLogger(GritConfig.class);

    static final String TML_MARK_SERVER = "${server}";
    static final String TML_MARK_SCHEMA = "${schema}";
    static final String TML_JDBC_URL = "jdbc:mysql://${server}/${schema}?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true";


    @Bean("grit.cache")
    public CacheService cache(@Inject(value = "${grit.cache}", required = false) Properties props) {
        if (props.size() > 0) {
            CacheServiceSupplier supplier = new CacheServiceSupplier(props);
            return new CacheServiceWrap(supplier.get());
        } else {
            return new CacheServiceWrap(new LocalCacheService());
        }
    }

    @Bean("grit.db")
    public DbContext db(@Inject(value = "${grit.db}", required = false) Properties props) {
        if (props.size() > 0) {

            String server = props.getProperty("server");
            String url = props.getProperty("url");

            if (Utils.isNotEmpty(server)) {
                String schema = props.getProperty("schema");
                if (Utils.isEmpty(schema)) {
                    schema = "grit";
                }

                props.remove("server");
                url = TML_JDBC_URL
                        .replace(TML_MARK_SERVER, server)
                        .replace(TML_MARK_SCHEMA, schema);
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
    public LdapClient ldapClient(@Inject(value = "${grit.ldap}", required = false) Properties props) {
        if (props.size() > 0) {
            return new LdapClient(props);
        } else {
            return null;
        }
    }

    @Bean
    public GritClient gritClient(@Inject(value = "${grit.db}", required = false) Properties props,
                                 @Inject GritClientLocalImpl clientLocal) {
        if (props.size() > 0) {
            log.info("Grit local mode will be used");
            GritClient.setGlobal(clientLocal);
            return clientLocal;
        } else {
            log.info("Grit cloud mode will be used");
            return null;
        }
    }
}
