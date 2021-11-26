package gritadmin;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Import;

/**
 * @author noear 2021/6/24 created
 */
public class AdminApp {
    public static void main(String[] args) {
        Solon.start(AdminApp.class, args, app -> {
            app.cfg().loadEnv("grit.");
        });
    }
}
