package gritapi;

import org.noear.solon.Solon;

/**
 * @author noear
 * @since 1.0
 */
public class ApiApp {
    public static void main(String[] args) {
        Solon.start(ApiApp.class, args, app -> {
            app.cfg().loadEnv("grit");
            app.onError(e -> e.printStackTrace());
        });
    }
}
