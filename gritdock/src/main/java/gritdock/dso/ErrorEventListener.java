package gritdock.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventListener;
import org.noear.solon.core.handle.Context;
import org.noear.solon.logging.utils.TagsMDC;

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

        if (ctx != null) {
            TagsMDC.tag1(ctx.path());
        }

        log.error("bcfdock {}", err);
    }
}
