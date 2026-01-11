package com.cryptotracker.backend.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummaryDto {

    private double totalValue;
    private double totalProfitLoss;
    private String bestAsset;
    private int assetCount;
}
