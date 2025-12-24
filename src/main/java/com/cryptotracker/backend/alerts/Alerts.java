package com.cryptotracker.backend.alerts;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "price_alerts")
@Data
public class Alerts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    // CoinGecko ID → bitcoin, ethereum, solana
    private String coinId;

    private double targetPriceInr;

    @Enumerated(EnumType.STRING)
    private AlertType type; // ABOVE / BELOW

    private boolean triggered = false;

    private LocalDateTime createdAt;
}
