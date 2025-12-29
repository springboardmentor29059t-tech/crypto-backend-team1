package com.crypto.backend.market;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {

    // Fetch history for charts (e.g., last 7 days)
    List<PriceSnapshot> findByAssetSymbolAndCapturedAtAfterOrderByCapturedAtAsc(
            String assetSymbol, LocalDateTime startTime
    );
}
