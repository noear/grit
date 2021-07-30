package example1;

import org.noear.solon.Solon;
import org.noear.solon.SolonBuilder;

/**
 * @author noear 2021/6/1 created
 */
public class Example1App {
    public static void main(String[] args) {
        new SolonBuilder()
                .onError(e->e.printStackTrace())
                .start(Example1App.class, args);
    }
}
