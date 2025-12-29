package com.crypto.backend.market;

import com.crypto.backend.portfolio.Holding;
import com.crypto.backend.portfolio.HoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

        if (assets.isEmpty()) return;

        // 2. Fetch full market data (Price + Market Cap)
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
                    "CoinGecko" // Source field
            );

            priceSnapshotRepository.save(snapshot);
        }
        System.out.println("Captured snapshots for " + marketData.size() + " assets.");
    }

    public List<PriceSnapshot> getHistory(String symbol) {
        return priceSnapshotRepository.findByAssetSymbolAndCapturedAtAfterOrderByCapturedAtAsc(
                symbol, LocalDateTime.now().minusDays(7)
        );
    }
}
