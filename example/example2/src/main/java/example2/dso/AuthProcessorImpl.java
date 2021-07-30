package example2.dso;

import org.noear.solon.extend.grit.GritAuthProcessor;

/**
 * @author noear 2021/5/28 created
 */
public class AuthProcessorImpl extends GritAuthProcessor {
    @Override
    protected long getUserId() {
        return Session.current().getUserId();
    }
}
