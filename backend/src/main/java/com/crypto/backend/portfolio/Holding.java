package com.crypto.backend.portfolio;

import com.crypto.backend.exchange.Exchange;
import com.crypto.backend.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "holdings")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, name = "asset_symbol")
    private String assetSymbol;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal quantity;

    @Column(name = "avg_cost", precision = 19, scale = 8)
    private BigDecimal avgCost; // For calculating P&L later

    // Instead of a complex Java ENUM, let's store it as a String for simplicity: "EXCHANGE" or "WALLET"
    @Column(name = "wallet_type")
    private String walletType;

    @ManyToOne
    @JoinColumn(name = "exchange_id") // Nullable, because it might be a personal wallet
    private Exchange exchange;

    @Column(name = "wallet_address")
    private String address; // Null if it's an exchange holding

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- Constructors ---
    public Holding() {}

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getAssetSymbol() { return assetSymbol; }
    public void setAssetSymbol(String assetSymbol) { this.assetSymbol = assetSymbol; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getAvgCost() { return avgCost; }
    public void setAvgCost(BigDecimal avgCost) { this.avgCost = avgCost; }

    public String getWalletType() { return walletType; }
    public void setWalletType(String walletType) { this.walletType = walletType; }

    public Exchange getExchange() { return exchange; }
    public void setExchange(Exchange exchange) { this.exchange = exchange; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
