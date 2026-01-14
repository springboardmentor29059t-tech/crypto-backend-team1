package com.cryptotracker.backend.reports;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PnLSummaryDto {
    private double totalInvested;
    private double currentValue;
    private double unrealizedPnL;
    private double realizedPnL;
}
