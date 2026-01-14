package com.cryptotracker.backend.pricing;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceHistoryController {

    private final PriceSnapshotRepository repository;

    // 🔹 Historical prices for charts
    @GetMapping("/history/{symbol}")
    public List<PriceSnapshot> getPriceHistory(
            @PathVariable String symbol
    ) {
        return repository
                .findTop30ByAssetSymbolOrderByCapturedAtAsc(symbol.toUpperCase());
    }
}
