package org.noear.grit.server.dso;

import org.noear.grit.model.data.ResourceDo;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;

import java.util.List;

/**
 * ResourceSpace 记忆功能
 *
 * @author noear
 * @since 1.0
 */
public class ResourceSpaceCookie {
    private static final String COOKIE_KEY = "grit_log__space";

    public static <T extends ResourceDo> long build(long spaceId, List<T> tags) {
        if (spaceId == 0L) {
            spaceId = get();
        }

        if (spaceId > 0) {
            long spaceId2 = spaceId;
            if (tags.stream().anyMatch(m -> spaceId2 == m.resource_id) == false) {
                spaceId = 0L;
            }
        }

        if (spaceId == 0 && tags.isEmpty() == false) {
            spaceId = tags.get(0).resource_id;
        }

        return spaceId;
    }

    public static long get() {
        String tmp = Context.current().cookie(COOKIE_KEY);

        if (Utils.isNotEmpty(tmp)) {
            return Long.parseLong(tmp);
        } else {
            return 0;
        }
    }

    public static void set(long space_id) {
        if (space_id == 0) {
            return;
        }

        Context.current().cookieSet(COOKIE_KEY, String.valueOf(space_id));
    }
}
