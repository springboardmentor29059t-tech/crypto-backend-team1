package com.crypto.backend.market;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_snapshots")
public class PriceSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_symbol", nullable = false)
    private String assetSymbol;

    @Column(name = "price_usd", nullable = false, precision = 18, scale = 8)
    private BigDecimal priceUsd;

    @Column(name = "market_cap", precision = 20, scale = 2)
    private BigDecimal marketCap;       // Added as per doc

    @Column(name = "source")
    private String source;              // Added as per doc (e.g., "CoinGecko")

    @Column(name = "captured_at")
    private LocalDateTime capturedAt;

    // Constructors
    public PriceSnapshot() {}

    public PriceSnapshot(String assetSymbol, BigDecimal priceUsd, BigDecimal marketCap, String source) {
        this.assetSymbol = assetSymbol;
        this.priceUsd = priceUsd;
        this.marketCap = marketCap;
        this.source = source;
        this.capturedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getAssetSymbol() { return assetSymbol; }
    public BigDecimal getPriceUsd() { return priceUsd; }
    public BigDecimal getMarketCap() { return marketCap; }
    public String getSource() { return source; }
    public LocalDateTime getCapturedAt() { return capturedAt; }
}
