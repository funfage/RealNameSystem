package com.real.name.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES encrypt and decrypt util
 */
public final class AesUtils {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String AES_NAME = "AES";
    private static final String ALGORITHM = "AES/CBC/PKCS7Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * encrypt
     */
    public static String encrypt( String content, String key) {
        if (!StringUtils.hasText(content)) {
            return null;
        }
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(subBytes(key.getBytes(CHARSET_NAME)));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            result = cipher.doFinal(content.getBytes(CHARSET_NAME));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Base64.encodeBase64String(result);
    }

    /**
     * decrypt
     */
    public static String decrypt( String content, String key) {
        if (!StringUtils.hasText(content)) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(subBytes(key.getBytes(CHARSET_NAME)));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            return new String(cipher.doFinal(Base64.decodeBase64(content)), CHARSET_NAME);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "error";
    }

    private static byte[] subBytes(byte[] src) {
        if (src.length < 16) {
            throw new RuntimeException("can not from Key get offset!");
        }
        byte[] bs = new byte[16];
        System.arraycopy(src, 0, bs, 0, 16);
        return bs;
    }
}