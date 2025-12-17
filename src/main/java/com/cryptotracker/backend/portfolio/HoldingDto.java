package com.cryptotracker.backend.portfolio;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HoldingDto {

    private String asset;
    private double quantity;
    private double avgBuyPrice;
}
