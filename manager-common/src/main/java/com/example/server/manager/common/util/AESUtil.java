package com.example.server.manager.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @date 2017-10-01
 */
public class AESUtil {

    private static final Logger logger = LoggerFactory.getLogger(AESUtil.class);
    private final static int KEY_LENGTH = 128;
    public static final String DEFAULT_ENCRYPT_KEY = "880421f5-5910-4068-8d12-3597157d61cf";

    private AESUtil() {
    }

    private static KeyGenerator getKeyGenerator() throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance("AES");
    }

    private static SecureRandom getSecureRandom() throws NoSuchAlgorithmException {
        return SecureRandom.getInstance("SHA1PRNG");
    }

    /**
     * 创建密码器
     */
    private static Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES");
    }

    private static SecretKeySpec getSecretKeySpec(String encryptKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        SecureRandom secureRandom = getSecureRandom();
        secureRandom.setSeed(encryptKey.getBytes("UTF-8"));
        KeyGenerator keyGen = getKeyGenerator();
        keyGen.init(KEY_LENGTH, secureRandom);
        SecretKey secretKey = keyGen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        return new SecretKeySpec(enCodeFormat, "AES");
    }

    /**
     * AES 加密
     *
     * @param inStr      加密内容
     * @param encryptKey 加密秘钥
     * @return 加密后的数组
     */
    public static byte[] encrypt(String inStr, String encryptKey) {
        try {
            Cipher cipher = getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(encryptKey));
            return cipher.doFinal(inStr.getBytes("UTF-8"));
        } catch (Exception e) {
            logger.error("AESUtil.encrypt error, inStr:{}", inStr, e);
        }
        return new byte[0];
    }

    /**
     * AES 解密
     *
     * @param inByte     解密内容
     * @param encryptKey 解密秘钥
     * @return 解密后的字符串
     */
    public static String decrypt(byte[] inByte, String encryptKey) {
        try {
            Cipher cipher = getCipher();
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(encryptKey));
            return new String(cipher.doFinal(inByte), "UTF-8");
        } catch (Exception e) {
            logger.error("AESUtil.decrypt error, inByte:{}", Arrays.toString(inByte), e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * AES 加密
     *
     * @param inStr      加密内容
     * @param encryptKey 加密秘钥
     * @return 加密后的hex字符串
     */
    public static String encryptHex(String inStr, String encryptKey) {
        return HexUtil.toHexString(encrypt(inStr, encryptKey));
    }

    /**
     * AES 解密
     *
     * @param hexStr     解密hex字符串
     * @param encryptKey 解密秘钥
     * @return 解密后的字符串
     */
    public static String decryptHex(String hexStr, String encryptKey) {
        return decrypt(HexUtil.toByteArray(hexStr), encryptKey);
    }

    /*
    public static void main(String[] args) {
        String s = "管理员admin";
        System.out.println("原始:" + s);
        System.out.println("AES加密后:" + encryptHex(s, "123"));
        System.out.println("AES解密后:" + decryptHex("5c70eb9357a1691d5021e4f38d6b388d", "123"));
    }*/
}
