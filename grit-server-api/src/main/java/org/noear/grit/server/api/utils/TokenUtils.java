package org.noear.grit.server.api.utils;

import org.noear.grit.client.utils.EncryptUtils;
import org.noear.snack.ONode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author noear
 * @since 1.0
 */
public class TokenUtils {
    private static final String key = "H#WcZn_5V_*DBU(r";

    public static String encode(long subjectId) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("i", subjectId);
        data.put("t", System.currentTimeMillis());

        String dataJson = ONode.stringify(data);

        //加密
        try {
            return EncryptUtils.aesEncrypt(dataJson, key);
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    public static long decode(String token) throws IOException {
        try {
            //解密
            String dataJson = EncryptUtils.aesDecrypt(token, key);

            ONode oNode = ONode.loadStr(dataJson);
            long subjectId = oNode.get("i").getLong();
            long time = oNode.get("t").getLong();

            if (subjectId > 0L) {
                if (Math.abs(time - System.currentTimeMillis()) < 60_000) {
                    //60秒内有效
                    return subjectId;
                } else {
                    //超时
                    return 0L;
                }
            } else {
                //令牌无效（需要重新登录）
                return -1L;
            }
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }
}