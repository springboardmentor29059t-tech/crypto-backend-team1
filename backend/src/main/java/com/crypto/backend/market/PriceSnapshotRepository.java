package com.crypto.backend.market;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {

    // CHANGED: Removed "AndCapturedAtAfter" to ensure you get data regardless of time issues.
    // We just get all snapshots for the symbol, sorted by time.
    List<PriceSnapshot> findByAssetSymbolOrderByCapturedAtAsc(String assetSymbol);
}
