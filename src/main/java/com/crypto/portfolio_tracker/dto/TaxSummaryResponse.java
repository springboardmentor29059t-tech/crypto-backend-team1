package com.crypto.portfolio_tracker.dto;

public class TaxSummaryResponse {

    private String assetSymbol;
    private Double investedValue;
    private Double currentValue;
    private Double unrealizedPnl;

    // Getters & Setters
    public String getAssetSymbol() {
        return assetSymbol;
    }

    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public Double getInvestedValue() {
        return investedValue;
    }

    public void setInvestedValue(Double investedValue) {
        this.investedValue = investedValue;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public Double getUnrealizedPnl() {
        return unrealizedPnl;
    }

    public void setUnrealizedPnl(Double unrealizedPnl) {
        this.unrealizedPnl = unrealizedPnl;
    }
}
