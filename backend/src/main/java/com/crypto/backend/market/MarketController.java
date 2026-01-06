package com.crypto.backend.market;

import com.crypto.backend.apikey.ApiKey;
import com.crypto.backend.apikey.ApiKeyService;
import com.crypto.backend.portfolio.Holding;
import com.crypto.backend.portfolio.HoldingRequest;
import com.crypto.backend.portfolio.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired; // Added
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

    // NEW: Inject the PriceSnapshotService
    @Autowired
    private PriceSnapshotService priceSnapshotService;

    public MarketController(ApiKeyService apiKeyService,
                            BinanceConnector binanceConnector,
                            PortfolioService portfolioService) {
        this.apiKeyService = apiKeyService;
        this.binanceConnector = binanceConnector;
        this.portfolioService = portfolioService;
    }

    // --- EXISTING METHODS (Weeks 1-4) ---

    @GetMapping("/api/coins")
    public String getCoins(@RequestParam String currency) {
        String url = "https://api.coingecko.com/api/v3/coins/markets"
                + "?vs_currency=" + currency
                + "&order=market_cap_desc&per_page=100&page=1&sparkline=false";
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/api/portfolio/{userId}")
    public List<Holding> getPortfolio(@PathVariable Long userId) {
        List<ApiKey> keys = apiKeyService.getKeysByUserId(userId);

        for (ApiKey key : keys) {
            String decryptedSecret = apiKeyService.getDecryptedSecret(key);

            if (key.getExchange().getId() == 1) { // Binance
                Map<String, BigDecimal> balances =
                        binanceConnector.getBalances(key.getApiKey(), decryptedSecret);

                portfolioService.syncBalances(userId, key.getExchange().getId(), balances);
            }
        }
        return portfolioService.getHoldings(userId);
    }

    @PostMapping("/api/portfolio/{userId}/manual")
    public Holding addManualHolding(@PathVariable Long userId,
                                    @RequestBody HoldingRequest request) {
        return portfolioService.manualAddOrUpdate(userId, request);
    }

    @PostMapping("/api/portfolio/{userId}/recompute-avgcost")
    public List<Holding> recomputeAvgCost(@PathVariable Long userId) {
        portfolioService.updateAverageCostForUser(userId);
        return portfolioService.getHoldings(userId);
    }

    // --- NEW METHODS (Week 5: Charts & Snapshots) ---

    // Get chart history: GET /api/charts/BTC?period=7d -> prev (for 7days)
    // new chart data fetch (below)
    @GetMapping("/api/charts/{symbol}")
    public List<PriceSnapshot> getChart(@PathVariable String symbol) {
        // Use the new simpler query
        return priceSnapshotService.getHistory(symbol.toUpperCase());
    }


    // Manual snapshot trigger: POST /api/snapshots/capture
    @PostMapping("/api/snapshots/capture")
    public String manualCapture() {
        priceSnapshotService.capturePrices();
        return "Prices captured manually for all holdings!";
    }
}
