package com.cryptotracker.backend.pricing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PriceSnapshotService {

    private final PriceSnapshotRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    public void capturePrices() {

        String url =
            "https://api.coingecko.com/api/v3/simple/price" +
            "?ids=bitcoin,ethereum,solana,binancecoin" +
            "&vs_currencies=inr" +
            "&include_market_cap=true";

        Map<String, Map<String, Object>> response =
                restTemplate.getForObject(url, Map.class);

        saveSnapshot("BTC", response.get("bitcoin"));
        saveSnapshot("ETH", response.get("ethereum"));
        saveSnapshot("SOL", response.get("solana"));
        saveSnapshot("BNB", response.get("binancecoin"));
    }

    private void saveSnapshot(String symbol, Map<String, Object> data) {

        if (data == null) return;

        Number priceInr = (Number) data.get("inr");
        Number marketCapInr = (Number) data.get("inr_market_cap");

        PriceSnapshot snapshot = new PriceSnapshot();
        snapshot.setAssetSymbol(symbol);
        snapshot.setPriceInr(priceInr != null ? priceInr.doubleValue() : 0.0);
        snapshot.setMarketCapInr(marketCapInr != null ? marketCapInr.doubleValue() : 0.0);
        snapshot.setSource("CoinGecko");
        snapshot.setCapturedAt(LocalDateTime.now());

        repository.save(snapshot);
    }
}
