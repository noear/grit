package demo;

import org.noear.solon.Solon;

/**
 * @author noear 2021/6/1 created
 */
public class DemoApp {
    public static void main(String[] args) {
        Solon.start(DemoApp.class, args);

        /**
         * 此Demo只能看，不能运行；其中，BcfClient 需要初始化数据库和缓存;
         * */
    }
}
