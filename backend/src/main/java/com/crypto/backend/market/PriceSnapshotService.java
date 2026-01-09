package com.crypto.backend.market;

import com.crypto.backend.portfolio.Holding;
import com.crypto.backend.portfolio.HoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent; // Import
import org.springframework.context.event.EventListener;             // Import
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceSnapshotService {

    @Autowired private PriceSnapshotRepository priceSnapshotRepository;
    @Autowired private HoldingRepository holdingRepository;
    @Autowired private CoinGeckoService coinGeckoService;

    // Runs every hour
    @Scheduled(fixedRate = 3600000)
    public void capturePrices() {
        // 1. Get unique assets from portfolio
        List<String> assets = holdingRepository.findAll().stream()
                .map(Holding::getAssetSymbol)
                .distinct()
                .collect(Collectors.toList());

        if (assets.isEmpty()) {
            System.out.println("No holdings found to fetch prices for.");
            return;
        }

        // 2. Fetch full market data (Price + Market Cap)
        System.out.println("Fetching prices for: " + assets);
        List<Map<String, Object>> marketData = coinGeckoService.getMarketData(assets);

        // 3. Save to DB
        for (Map<String, Object> data : marketData) {
            String id = (String) data.get("id");
            String symbol = coinGeckoService.getSymbolFromId(id);

            // Extract numbers safely
            Double price = Double.valueOf(data.get("current_price").toString());
            Double mCap = data.get("market_cap") != null ?
                    Double.valueOf(data.get("market_cap").toString()) : 0.0;

            PriceSnapshot snapshot = new PriceSnapshot(
                    symbol,
                    BigDecimal.valueOf(price),
                    BigDecimal.valueOf(mCap),
                    "CoinGecko"
            );

            priceSnapshotRepository.save(snapshot);
        }
        System.out.println("Captured snapshots for " + marketData.size() + " assets.");
    }

    // NEW: Auto-run on startup
    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        System.out.println("App Started. Running initial price capture...");
        capturePrices();
    }

    public List<PriceSnapshot> getHistory(String symbol) {
        return priceSnapshotRepository.findByAssetSymbolOrderByCapturedAtAsc(symbol);
    }
}
