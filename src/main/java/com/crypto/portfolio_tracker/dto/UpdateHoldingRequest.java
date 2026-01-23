package com.crypto.portfolio_tracker.dto;

public class UpdateHoldingRequest {

    private Double quantity;
    private Double avgCost;

    // Getters and Setters

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(Double avgCost) {
        this.avgCost = avgCost;
    }
}
