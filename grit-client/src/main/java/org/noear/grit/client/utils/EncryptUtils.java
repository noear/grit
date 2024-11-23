package org.noear.grit.client.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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

    //
    // aesEncrypt , aesDecrypt
    //
    public static String aesEncrypt(String content, String password) throws Exception {
        return aesEncrypt(content, password, null);
    }

    public static String aesEncrypt(String content, String password, String algorithm) throws Exception {
        return aesEncrypt(content, password, algorithm, null);
    }

    public static String aesEncrypt(String content, String password, String algorithm, String offset) throws Exception {
        return aesEncrypt(content, password, algorithm, offset, null);
    }

    public static String aesEncrypt(String content, String password, String algorithm, String offset, String charset) throws Exception {

        if (TextUtils.isEmpty(algorithm)) {
            algorithm = "AES/ECB/PKCS5Padding";
        }

        if (TextUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }

        byte[] pswd = password.getBytes(charset);
        SecretKeySpec secretKey = new SecretKeySpec(pswd, "AES");
        Cipher cipher = Cipher.getInstance(algorithm);
        if (TextUtils.isEmpty(offset)) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes(charset));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        }

        byte[] encrypted = cipher.doFinal(content.getBytes(charset));
        return Base64Utils.encodeByte(encrypted);//new Base64()).encodeToString(encrypted);

    }

    public static String aesDecrypt(String content, String password) throws Exception {
        return aesDecrypt(content, password, null);
    }

    public static String aesDecrypt(String content, String password, String algorithm) throws Exception {
        return aesDecrypt(content, password, algorithm, null);
    }

    public static String aesDecrypt(String content, String password, String algorithm, String offset) throws Exception {
        return aesDecrypt(content, password, algorithm, offset, null);
    }

    public static String aesDecrypt(String content, String password, String algorithm, String offset, String charset) throws Exception {

        if (TextUtils.isEmpty(algorithm)) {
            algorithm = "AES/ECB/PKCS5Padding";
        }

        if (TextUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }

        byte[] pswd = password.getBytes(charset);
        SecretKey secretKey = new SecretKeySpec(pswd, "AES");

        //密码
        Cipher cipher = Cipher.getInstance(algorithm);
        if (TextUtils.isEmpty(offset)) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } else {
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes(charset));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        }

        byte[] encrypted1 = Base64Utils.decodeByte(content); //(new Base64()).decode(content);
        byte[] original = cipher.doFinal(encrypted1);

        return new String(original, charset);
    }
}
