package com.crypto.backend.market;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {

    // Get all snapshots for the symbol, sorted by time (Week 5)
    List<PriceSnapshot> findByAssetSymbolOrderByCapturedAtAsc(String assetSymbol);

    // Get LATEST price snapshot for symbol (FOR P&L)
    List<PriceSnapshot> findFirstByAssetSymbolOrderByCapturedAtDesc(String assetSymbol);
}
