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
    public static <T extends ResourceDo> long build(long spaceId, List<T> tags) {
        if (spaceId == 0L) {
            String tmp = cookieGet();
            if (Utils.isNotEmpty(tmp)) {
                spaceId = Long.parseLong(tmp);
            }
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

    public static String cookieGet(){
        return Context.current().cookie("grit_log__space");
    }

    public static void cookieSet(String tag){
        if(Utils.isEmpty(tag)){
            return;
        }

        Context.current().cookieSet("grit_log__space", tag);
    }
}
