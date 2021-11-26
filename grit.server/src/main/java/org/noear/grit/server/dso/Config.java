package org.noear.grit.server.dso;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.grit.client.GritClient;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.cache.CacheServiceSupplier;
import org.noear.solon.data.cache.CacheService;

import javax.sql.DataSource;

/**
 * @author noear
 * @since 1.0
 */
@Configuration
public class Config {
    @Bean(value = "grit.cache", typed = true)
    public CacheService cache(@Inject("${grit.cache}") CacheServiceSupplier supplier) {
        return new CacheServiceWrap(supplier.get());
    }

    @Bean(value = "grit.db", typed = true)
    public DataSource db(@Inject("${grit.db}") HikariDataSource ds) {
        return ds;
    }

    @Bean
    public void clientImpl(@Inject GritClientLocalImpl clientLocal) {
        GritClient.setGlobal(clientLocal);
    }
}
