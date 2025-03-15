package com.bjrltcp.tools;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;

public class DESTools {

    private static final String secretKeyString="AABBCCDD";

    public static void main(String[] args) {
        try {
            String originalMessage = "Hello, DES-ECB with PKCS#7 Padding!";

            // 生成DES密钥
            SecretKey secretKey = generateSecret(secretKeyString);

            // 加密
            byte[] encryptedBytes = desEncodeData(originalMessage);

            // 解密
            String decryptedMessage = desDecodeData(encryptedBytes);
            // 显示解密后的原始数据
            System.out.println("Decrypted Message: " + decryptedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SecretKey generateSecret(String secretKeyString) throws Exception {
        byte[] secretKeyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        DESKeySpec desKeySpec = new DESKeySpec(secretKeyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        return secretKey;
    }

    public static byte[] desEncodeData(byte[] bytes) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, generateSecret(secretKeyString));
        byte[] encryptedBytes = cipher.doFinal(bytes);
        return encryptedBytes;
    }

    public static byte[] desEncodeData(String content) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, generateSecret(secretKeyString));
        byte[] encryptedBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return encryptedBytes;
    }

    public static String desDecodeData(byte[] encryptedBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, generateSecret(secretKeyString));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedMessage = new String(decryptedBytes, StandardCharsets.UTF_8);
        return decryptedMessage;
    }

}