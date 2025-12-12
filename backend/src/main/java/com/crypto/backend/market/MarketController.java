package com.crypto.backend.market;

import com.crypto.backend.apikey.ApiKey;
import com.crypto.backend.apikey.ApiKeyService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MarketController {

    private final RestTemplate restTemplate = new RestTemplate();

    // Dependencies we need to fetch keys and talk to Binance
    private final ApiKeyService apiKeyService;
    private final BinanceConnector binanceConnector;

    // Constructor Injection
    public MarketController(ApiKeyService apiKeyService, BinanceConnector binanceConnector) {
        this.apiKeyService = apiKeyService;
        this.binanceConnector = binanceConnector;
    }

    // --- EXISTING ENDPOINT (Keep this) ---
    @GetMapping("/api/coins")
    public String getCoins(@RequestParam String currency) {
        String url = "https://api.coingecko.com/api/v3/coins/markets"
                + "?vs_currency=" + currency
                + "&order=market_cap_desc&per_page=100&page=1&sparkline=false";
        return restTemplate.getForObject(url, String.class);
    }

    // --- NEW ENDPOINT: Get Portfolio Balance ---
    @GetMapping("/api/portfolio/{userId}")
    public Map<String, BigDecimal> getPortfolio(@PathVariable Long userId) {
        // 1. Get all API keys for this user
        List<ApiKey> keys = apiKeyService.getKeysByUserId(userId);

        Map<String, BigDecimal> totalPortfolio = new HashMap<>();

        for (ApiKey key : keys) {
            // 2. Decrypt the secret
            String decryptedSecret = apiKeyService.getDecryptedSecret(key);

            // 3. If it's Binance (we assume Exchange ID 1 is Binance for now), call it
            // In a real app, you'd check key.getExchange().getName().equals("Binance")
            if (key.getExchange().getId() == 1) {
                Map<String, BigDecimal> balances = binanceConnector.getBalances(key.getApiKey(), decryptedSecret);

                // 4. Merge balances (e.g., if you have BTC on two accounts)
                balances.forEach((asset, amount) ->
                        totalPortfolio.merge(asset, amount, BigDecimal::add)
                );
            }
        }

        return totalPortfolio;
    }
}
