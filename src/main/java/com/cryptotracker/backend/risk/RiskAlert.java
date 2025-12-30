package com.cryptotracker.backend.risk;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "risk_alerts")
@Data
public class RiskAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String asset; // BTC, ETH, SOL

    private String riskLevel; // SAFE / WARNING

    private String message;

    private LocalDateTime createdAt;
}
