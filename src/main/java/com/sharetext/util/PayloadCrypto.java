package com.sharetext.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PayloadCrypto {

    // 32-byte key for AES-256
    private static final byte[] SECRET_KEY = {
            11, 22, 33, 44, 55, 66, 77, 88, 99, 10, 11, 12, 13, 14, 15, 16,
            17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32
    };

    public static String decrypt(String encryptedText) throws Exception {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return encryptedText; // Return as is if empty
        }
        
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedText);
            
            // Extract IV
            byte[] iv = new byte[16];
            System.arraycopy(combined, 0, iv, 0, 16);
            
            // Extract ciphertext
            byte[] cipherText = new byte[combined.length - 16];
            System.arraycopy(combined, 16, cipherText, 0, cipherText.length);
            
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            
            byte[] decryptedBytes = cipher.doFinal(cipherText);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            // Not pure base64 or other issue
            throw new Exception("Invalid encrypted payload format");
        }
    }
}
