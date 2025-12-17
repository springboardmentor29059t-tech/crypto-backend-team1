package com.crypto.backend.portfolio;

import java.math.BigDecimal;

public class HoldingRequest {
    private String assetSymbol; // e.g., "BTC"
    private BigDecimal quantity; // e.g., 2.5
    private String walletType;   // "WALLET" or "EXCHANGE"
    private Long exchangeId;     // Optional (can be null if it's a personal wallet)
    private String address;      // Optional (wallet address)

    // Getters and Setters
    public String getAssetSymbol() { return assetSymbol; }
    public void setAssetSymbol(String assetSymbol) { this.assetSymbol = assetSymbol; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getWalletType() { return walletType; }
    public void setWalletType(String walletType) { this.walletType = walletType; }

    public Long getExchangeId() { return exchangeId; }
    public void setExchangeId(Long exchangeId) { this.exchangeId = exchangeId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
