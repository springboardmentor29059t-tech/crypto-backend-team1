package com.crypto.portfolio_tracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.portfolio_tracker.model.PriceSnapshots;
import com.crypto.portfolio_tracker.repository.PriceSnapshotsRepository;
import com.crypto.portfolio_tracker.service.CoinGeckoService;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final CoinGeckoService coinGeckoService;
    private final PriceSnapshotsRepository priceSnapshotsRepository;

    public PriceController(CoinGeckoService coinGeckoService,
                           PriceSnapshotsRepository priceSnapshotsRepository) {
        this.coinGeckoService = coinGeckoService;
        this.priceSnapshotsRepository = priceSnapshotsRepository;
    }

    // Get live price and store snapshot
    @GetMapping("/fetch/{coinId}")
    public ResponseEntity<Double> fetchPrice(@PathVariable String coinId) {

        Double priceUsd = coinGeckoService.getCurrentPrice(coinId);

        PriceSnapshots snapshot = new PriceSnapshots();
        snapshot.setAssetSymbol(coinId.toUpperCase());
        snapshot.setPriceUsd(priceUsd);
        snapshot.setSource("CoinGecko");

        priceSnapshotsRepository.save(snapshot);

        return ResponseEntity.ok(priceUsd);
    }

    // Get price history from DB
    @GetMapping("/history/{assetSymbol}")
    public ResponseEntity<List<PriceSnapshots>> getPriceHistory(
            @PathVariable String assetSymbol) {

        return ResponseEntity.ok(
            priceSnapshotsRepository
                .findByAssetSymbolOrderByCapturedAtDesc(assetSymbol)
        );
    }
}
