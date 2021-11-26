package org.noear.grit.client.impl.utils;

import java.security.MessageDigest;

/**
 * 安全工具
 *
 * @author noear
 * @since 1.0
 */
public class EncryptUtils {
    /**
     * sha1加密
     * */
    public static String sha1(String cleanData) {
        return hashEncode("SHA-1", cleanData);
    }

    /**
     * md5加密
     * */
    public static String md5(String cleanData) {
        return hashEncode("MD5", cleanData);
    }

    private static String hashEncode(String algorithm, String cleanData) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = cleanData.getBytes("UTF-16LE");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance(algorithm);
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
