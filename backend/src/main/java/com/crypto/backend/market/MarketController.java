package com.crypto.backend.market;

import com.crypto.backend.apikey.ApiKey;
import com.crypto.backend.apikey.ApiKeyService;
import com.crypto.backend.portfolio.Holding;
import com.crypto.backend.portfolio.HoldingRequest; // <--- Import this
import com.crypto.backend.portfolio.PortfolioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class MarketController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final ApiKeyService apiKeyService;
    private final BinanceConnector binanceConnector;
    private final PortfolioService portfolioService;

    // Constructor Injection
    public MarketController(ApiKeyService apiKeyService,
                            BinanceConnector binanceConnector,
                            PortfolioService portfolioService) {
        this.apiKeyService = apiKeyService;
        this.binanceConnector = binanceConnector;
        this.portfolioService = portfolioService;
    }

    // --- EXISTING ENDPOINT (Keep this) ---
    @GetMapping("/api/coins")
    public String getCoins(@RequestParam String currency) {
        String url = "https://api.coingecko.com/api/v3/coins/markets"
                + "?vs_currency=" + currency
                + "&order=market_cap_desc&per_page=100&page=1&sparkline=false";
        return restTemplate.getForObject(url, String.class);
    }

    // --- UPDATED ENDPOINT: Sync & Return Portfolio ---
    @GetMapping("/api/portfolio/{userId}")
    public List<Holding> getPortfolio(@PathVariable Long userId) {
        // 1. Get all API keys for this user
        List<ApiKey> keys = apiKeyService.getKeysByUserId(userId);

        for (ApiKey key : keys) {
            // 2. Decrypt the secret
            String decryptedSecret = apiKeyService.getDecryptedSecret(key);

            // 3. If it's Binance (ID 1)
            if (key.getExchange().getId() == 1) {
                // A. Fetch live balances from Binance
                Map<String, BigDecimal> balances = binanceConnector.getBalances(key.getApiKey(), decryptedSecret);

                // B. SAVE to Database using your new Service
                // This ensures we persist the data permanently
                portfolioService.syncBalances(userId, key.getExchange().getId(), balances);
            }
        }

        // 4. Return the data from OUR database
        return portfolioService.getHoldings(userId);
    }

    // --- NEW ENDPOINT: Manual Add ---
    @PostMapping("/api/portfolio/{userId}/manual")
    public Holding addManualHolding(@PathVariable Long userId, @RequestBody HoldingRequest request) {
        return portfolioService.manualAddOrUpdate(userId, request);
    }
}
