package com.crypto.portfolio_tracker.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Service
public class EncryptionService {

    // Fixed 16-character key (for demo / project)
    private static final String SECRET = "MySecretKey123456"; // 16 chars

    private SecretKeySpec getKeySpec() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        // Make sure length is exactly 16 bytes for AES-128
        keyBytes = Arrays.copyOf(keyBytes, 16);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getKeySpec());
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception ex) {
            ex.printStackTrace(); // print error in console
            throw new RuntimeException("Error encrypting", ex);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKeySpec());
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace(); // print error in console
            throw new RuntimeException("Error decrypting", ex);
        }
    }
}
