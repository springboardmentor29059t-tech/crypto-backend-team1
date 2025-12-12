package com.cryptotracker.backend.binance;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
public class BinanceSignatureService {

    private static final String HMAC_SHA256 = "HmacSHA256";

    /**
     * Returns lowercase hex string of HMAC-SHA256(data, secret)
     */
    public String generateSignature(String data, String secret) {
        try {
            Mac sha256Hmac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes("UTF-8"), HMAC_SHA256);
            sha256Hmac.init(keySpec);
            byte[] macData = sha256Hmac.doFinal(data.getBytes("UTF-8"));
            return bytesToHex(macData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC SHA256 signature", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
