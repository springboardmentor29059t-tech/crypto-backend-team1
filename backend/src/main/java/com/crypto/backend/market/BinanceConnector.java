package com.crypto.backend.market;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component("binanceConnector")
public class BinanceConnector implements ExchangeConnector {

    private static final String BASE_URL = "https://api.binance.com";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, BigDecimal> getBalances(String apiKey, String apiSecret) {
        Map<String, BigDecimal> balances = new HashMap<>();

        try {
            // 1. Prepare URL and Signature
            long timestamp = System.currentTimeMillis();
            String queryString = "timestamp=" + timestamp;
            String signature = hmacSha256(queryString, apiSecret);
            String fullUrl = BASE_URL + "/api/v3/account?" + queryString + "&signature=" + signature;

            // 2. Create Connection
            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-MBX-APIKEY", apiKey); // Required header

            // 3. Read Response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                // 4. Parse JSON using Jackson (Standard in Spring Boot)
                JsonNode rootNode = objectMapper.readTree(content.toString());
                JsonNode balancesNode = rootNode.get("balances");

                if (balancesNode != null && balancesNode.isArray()) {
                    for (JsonNode assetNode : balancesNode) {
                        String asset = assetNode.get("asset").asText();
                        BigDecimal free = new BigDecimal(assetNode.get("free").asText());
                        BigDecimal locked = new BigDecimal(assetNode.get("locked").asText());
                        BigDecimal total = free.add(locked);

                        // Only add assets where you actually have money
                        if (total.compareTo(BigDecimal.ZERO) > 0) {
                            balances.put(asset, total);
                        }
                    }
                }
            } else {
                System.err.println("Binance API Error: " + responseCode);
                // Optionally read error stream here for debugging
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return balances;
    }

    // Standard HMAC SHA256 Signature logic
    private String hmacSha256(String data, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(data.getBytes());

            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hash.append('0');
                hash.append(hex);
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Signing failed", e);
        }
    }
}
