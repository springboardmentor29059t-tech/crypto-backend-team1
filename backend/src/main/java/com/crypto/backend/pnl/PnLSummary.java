package com.crypto.backend.pnl;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pnl_summaries")
public class PnLSummary {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id") private Long userId;
    @Column(name = "total_unrealized_pnl", precision = 20, scale = 2) private BigDecimal totalUnrealizedPnl;
    @Column(name = "total_realized_pnl", precision = 20, scale = 2) private BigDecimal totalRealizedPnl;
    @Column(name = "calculated_at") private LocalDateTime calculatedAt;

    public PnLSummary() {}
    public PnLSummary(Long userId, BigDecimal unrealized, BigDecimal realized) {
        this.userId = userId;
        this.totalUnrealizedPnl = unrealized;
        this.totalRealizedPnl = realized;
        this.calculatedAt = LocalDateTime.now();
    }
    // Getters...
    public Long getUserId() { return userId; }
    public BigDecimal getTotalUnrealizedPnl() { return totalUnrealizedPnl; }
    public BigDecimal getTotalRealizedPnl() { return totalRealizedPnl; }
}
