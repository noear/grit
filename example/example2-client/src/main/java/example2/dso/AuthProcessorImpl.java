package example2.dso;

import org.noear.grit.solon.GritAuthProcessor;
import org.noear.solon.core.handle.Context;

/**
 * @author noear 2021/5/28 created
 */
public class AuthProcessorImpl extends GritAuthProcessor {
    @Override
    protected long getSubjectId() {
        return Context.current().sessionAsLong("user_id");
    }
}
