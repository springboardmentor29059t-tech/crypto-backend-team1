package com.cryptotracker.backend.binance;

import com.cryptotracker.backend.common.EncryptionService;
import com.cryptotracker.backend.exchange.UserApiKey;
import com.cryptotracker.backend.exchange.UserApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class BinanceConnectorService {

    private final UserApiKeyRepository userApiKeyRepository;
    private final EncryptionService encryptionService;

    /**
     * Tests whether the provided user/keyId pair can successfully call a signed Binance endpoint.
     * Uses /api/v3/account with timestamp+signature to validate both API Key and API Secret.
     *
     * @param userId user owner of the key
     * @param keyId  id of the UserApiKey entity
     * @return true if Binance returns HTTP 200, false otherwise
     */
    public boolean testConnection(Long userId, Long keyId) {

        // Fetch key from DB
        UserApiKey key = userApiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("Key not found"));

        // Ensure the key belongs to the user
        if (!key.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        // Decrypt stored values
        String apiKey = encryptionService.decrypt(key.getApiKeyValue());
        String apiSecret = encryptionService.decrypt(key.getApiSecretValue());

        try {
            // Build signed query: timestamp required
            long timestamp = System.currentTimeMillis();
            String query = "timestamp=" + timestamp;

            // Create signature (hex)
            String signature = hmacSha256Hex(apiSecret, query);

            String endpoint = "https://api.binance.com/api/v3/account?" + query + "&signature=" + signature;

            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-MBX-APIKEY", apiKey);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            // If 200, optionally read response (useful for logs)
            if (responseCode == 200) {
                // drain response to avoid leaking connections
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    // optional: System.out.println("Binance account response: " + sb.toString());
                }
                return true;
            } else {
                // read error stream for debugging (optional)
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream() == null
                                ? connection.getInputStream()
                                : connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    System.out.println("Binance test failed (code " + responseCode + "): " + sb.toString());
                } catch (Exception ignore) {
                }
                return false;
            }

        } catch (Exception e) {
            System.out.println("Binance test exception: " + e.getMessage());
            return false;
        }
    }

    /** HMAC SHA256 -> hex */
    private String hmacSha256Hex(String secret, String message) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(keySpec);
        byte[] macData = sha256Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(macData);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
