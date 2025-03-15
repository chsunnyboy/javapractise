package com.bjrltcp.tools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSATools {

    public static PublicKey getPublicKey() throws Exception {
        // 读取公钥文件
        InputStream inputStream = RSATools.class.getClassLoader().getResourceAsStream("com/btiot/bjrltcp/pemfile/rsa_public_key.pem");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        String pemPublicKey = byteArrayOutputStream.toString();

        String publicKeyPEM = pemPublicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);

        // 使用X509EncodedKeySpec创建公钥规格
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

        // 获取RSA密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // 生成公钥对象
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        return publicKey;
    }

   public static byte[] encryptRSA(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes());
    }

    public static String decryptRSA(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData);
    }
}