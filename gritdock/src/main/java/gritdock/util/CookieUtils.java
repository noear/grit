package gritdock.util;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;

import java.sql.SQLException;

/**
 * @author noear
 * @since 1.8
 */
public class CookieUtils {
    private static final String COOKIE_KEY = "_lLnQIO4W";

    /**
     * 获取
     */
    public static ResourceSpace getResourceSpace(Context ctx) throws SQLException {
        String spaceCode = ctx.cookie(COOKIE_KEY);

        if (Utils.isEmpty(spaceCode) == false) {
            return GritClient.global().resource().getSpaceByCode(spaceCode);
        } else {
            return null;
        }
    }

    /**
     * 记录
     */
    public static void logResourceSpace(Context ctx, String spaceCode) {
        ctx.cookieSet(COOKIE_KEY, spaceCode, 60 * 60 * 24 * 365);
    }
}
