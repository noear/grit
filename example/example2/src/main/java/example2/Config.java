package example2;

import org.noear.grit.client.GritClient;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;

/**
 * @author noear 2021/7/30 created
 */
@Configuration
public class Config {
    @Bean
    public void initGrit() {
        GritClient.branched(Solon.cfg().appName());
    }
}
