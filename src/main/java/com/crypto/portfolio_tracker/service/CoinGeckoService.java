package com.crypto.portfolio_tracker.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoinGeckoService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Double getCurrentPrice(String coinId) {
        try {
            String url =
                "https://api.coingecko.com/api/v3/simple/price?ids="
                + coinId + "&vs_currencies=usd";

            Map<String, Object> response =
                restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey(coinId)) {
                throw new RuntimeException("Invalid response from CoinGecko");
            }

            Map<String, Object> coinData =
                (Map<String, Object>) response.get(coinId);

            return Double.valueOf(coinData.get("usd").toString());

        } catch (Exception e) {
            e.printStackTrace(); // IMPORTANT: shows real error in console
            throw new RuntimeException("Failed to fetch price from CoinGecko");
        }
    }
}
