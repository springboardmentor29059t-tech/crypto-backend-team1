package com.crypto.backend.market;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.*;
import java.math.BigDecimal;

@Service
public class CoinGeckoService {

    // Using the 'markets' endpoint to get Price AND Market Cap
    private final String API_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=%s";
    private final RestTemplate restTemplate = new RestTemplate();

    // Map your symbols (BTC) to CoinGecko IDs (bitcoin)
    private final Map<String, String> symbolToId = Map.of(
            "BTC", "bitcoin",
            "ETH", "ethereum",
            "SOL", "solana",
            "BNB", "binancecoin",
            "DOGE", "dogecoin"
    );

    public List<Map<String, Object>> getMarketData(List<String> symbols) {
        StringBuilder ids = new StringBuilder();
        for (String s : symbols) {
            if (symbolToId.containsKey(s.toUpperCase())) {
                ids.append(symbolToId.get(s.toUpperCase())).append(",");
            }
        }

        if (ids.length() == 0) return new ArrayList<>();

        try {
            // CoinGecko returns a List of Objects
            ResponseEntity<List> response = restTemplate.getForEntity(
                    String.format(API_URL, ids), List.class
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("CoinGecko API Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Helper to find the Symbol key from the ID (e.g., "bitcoin" -> "BTC")
    public String getSymbolFromId(String id) {
        for (Map.Entry<String, String> entry : symbolToId.entrySet()) {
            if (entry.getValue().equals(id)) return entry.getKey();
        }
        return id.toUpperCase();
    }
}
