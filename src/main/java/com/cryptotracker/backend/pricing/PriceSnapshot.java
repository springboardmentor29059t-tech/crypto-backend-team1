package com.cryptotracker.backend.pricing;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "price_snapshots")
@Data
public class PriceSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_symbol", nullable = false)
    private String assetSymbol;

    @Column(name = "price_inr", nullable = false)
    private Double priceInr;

    @Column(name = "market_cap")
    private Double marketCapInr;

    private String source;

    @Column(name = "captured_at")
    private LocalDateTime capturedAt;
}
