package com.crypto.backend.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<Holding, Long> {

    List<Holding> findByUserId(Long userId);

    // Find if User 1 already has BTC on Exchange 1 (Binance)
    Optional<Holding> findByUserIdAndAssetSymbolAndExchangeId(Long userId, String assetSymbol, Long exchangeId);

    // Find holding by user + symbol (FOR P&L)
    Optional<Holding> findByUserIdAndAssetSymbol(Long userId, String assetSymbol);
}
