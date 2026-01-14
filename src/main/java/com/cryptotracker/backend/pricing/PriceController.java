package com.cryptotracker.backend.pricing;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceSnapshotRepository repository;

    // 🔹 Historical prices for an asset
    @GetMapping("/{asset}/history")
    public List<PriceSnapshot> getPriceHistory(
            @PathVariable String asset
    ) {
        return repository
                .findTop30ByAssetSymbolOrderByCapturedAtAsc(asset.toUpperCase());
    }
}
