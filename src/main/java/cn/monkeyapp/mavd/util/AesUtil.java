package cn.monkeyapp.mavd.util;

import cn.monkeyapp.mavd.common.manage.LogManager;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ECB 无向量加解密
 *
 * @author Corbett Zhang
 */
public class AesUtil {

    private static final Logger LOGGER = LogManager.getLogger(AesUtil.class);

    //使用AES-128-CBC加密模式，key需要为16位,key和iv可以相同！
    private static String KEY = "M4wxGI0PY7UJWxXK";
    private static String IV = "23gX56et90Wa14B6";

    /**
     * 分隔符
     */
    private static final String CODE = "\\$";

    /**
     * 加密信息，带过期时间
     *
     * @param username 用户名
     * @param password 密码
     * @param expire   过期时间，天
     * @return
     */
    public static String encode(String username, String password, int expire) {
        final Date adjust = DateUtils.adjust(null, Calendar.DATE, expire);
        String datetime = DateUtils.formatDate(adjust, DateUtils.yyyyMMdd);
        try {
            return encrypt(username + CODE + password + CODE + datetime, KEY, IV);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public static String encode(String... keys) {
        try {
            return encrypt(StringUtils.join(keys, CODE), KEY, IV);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public static String[] decode(String key) {
        try {
            final String decrypt = decrypt(key, KEY, IV).trim();
            return decrypt.replace(CODE, " ").split(" ");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 加密方法
     *
     * @param data 要加密的数据
     * @param key  加密key
     * @param iv   加密iv
     * @return 加密的结果
     */
    private static String encrypt(String data, String key, String iv) throws Exception {
        //"算法/模式/补码方式"NoPadding PkcsPadding
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        int blockSize = cipher.getBlockSize();
        byte[] dataBytes = data.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(plaintext);

        return new Base64().encodeToString(encrypted);
    }

    /**
     * 解密方法
     *
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv   解密iv
     * @return 解密的结果
     */
    private static String decrypt(String data, String key, String iv) throws Exception {
        byte[] encrypted1 = new Base64().decode(data);
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), new IvParameterSpec(iv.getBytes()));
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original);
    }


}