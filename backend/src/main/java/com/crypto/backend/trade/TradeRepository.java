package com.crypto.backend.trade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    // All trades for a user
    List<Trade> findByUserId(Long userId);

    // All trades for a user + asset (used later for cost basis)
    List<Trade> findByUserIdAndAssetSymbolOrderByExecutedAtAsc(Long userId, String assetSymbol);

    // Optional helper: trades after a given time (for "recent trades" sync)
    List<Trade> findByUserIdAndExchangeIdAndExecutedAtAfter(
            Long userId,
            Long exchangeId,
            LocalDateTime after
    );
}
