package example2;

import org.noear.solon.SolonBuilder;

/**
 * @author noear 2021/7/30 created
 */
public class Example2App {
    public static void main(String[] args) {
        new SolonBuilder()
                .onError(e->e.printStackTrace())
                .start(Example2App.class, args);
    }
}
