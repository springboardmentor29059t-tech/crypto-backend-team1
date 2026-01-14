package com.cryptotracker.backend.pricing;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PriceSnapshotRepository
        extends JpaRepository<PriceSnapshot, Long> {

    PriceSnapshot findTopByAssetSymbolOrderByCapturedAtDesc(String assetSymbol);

    // ✅ ADD THIS
    List<PriceSnapshot> findTop30ByAssetSymbolOrderByCapturedAtAsc(String assetSymbol);
}
