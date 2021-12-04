package gritdock.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventListener;
import org.noear.solon.core.handle.Context;
import org.noear.solon.logging.utils.TagsMDC;

import java.util.ServiceConfigurationError;

/**
 * 异常事件监听（系统未处理的异常）
 *
 * @author noear
 * @since 1.0
 */
@Slf4j
@Component
public class ErrorEventListener implements EventListener<Throwable> {
    @Override
    public void onEvent(Throwable err) {
        Context ctx = Context.current();
        err = Utils.throwableUnwrap(err);

        if (ctx != null) {
            String path = ctx.path();

            if (path.startsWith("/grit/api/")) {
                TagsMDC.tag0("gritapi");
            } else if (path.startsWith("/grit/")) {
                TagsMDC.tag0("gritadmin");
            } else {
                TagsMDC.tag0("gritdock");
            }

            TagsMDC.tag1(path);
        } else {
            if (err instanceof ServiceConfigurationError) {
                return;
            }
        }

        log.error("{}", err);
    }
}
