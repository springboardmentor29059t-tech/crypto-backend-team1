package com.crypto.backend.risk;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "risk_alerts")
public class RiskAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "asset_symbol", nullable = false)
    private String assetSymbol;

    @Column(name = "alert_type")
    private String alertType; // 'rugpull_warning', 'contract_risk', 'news'

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public RiskAlert() {}

    public RiskAlert(Long userId, String assetSymbol, String alertType, String details) {
        this.userId = userId;
        this.assetSymbol = assetSymbol;
        this.alertType = alertType;
        this.details = details;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getAssetSymbol() { return assetSymbol; }
    public String getAlertType() { return alertType; }
    public String getDetails() { return details; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
