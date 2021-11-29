package gritdock;

import org.noear.solon.Solon;

/**
 * @author noear
 * @since 1.0
 */
public class DockApp {
    public static void main(String[] args) {
        Solon.start(DockApp.class, args, app -> {
            app.cfg().loadEnv("grit");
            app.onError(e -> e.printStackTrace());
        });
    }
}
