package com.crypto.portfolio_tracker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.crypto.portfolio_tracker.dto.PortfolioSummaryResponse;
import com.crypto.portfolio_tracker.model.Holdings;
import com.crypto.portfolio_tracker.repository.HoldingsRepository;
import com.crypto.portfolio_tracker.repository.PriceSnapshotsRepository;

@Service
public class PortfolioService {

    private final HoldingsRepository holdingsRepository;
    private final PriceSnapshotsRepository priceSnapshotsRepository;

    public PortfolioService(HoldingsRepository holdingsRepository,
                            PriceSnapshotsRepository priceSnapshotsRepository) {
        this.holdingsRepository = holdingsRepository;
        this.priceSnapshotsRepository = priceSnapshotsRepository;
    }

    public List<PortfolioSummaryResponse> getPortfolioSummary(Long userId) {

        List<Holdings> holdingsList = holdingsRepository.findByUserId(userId);
        List<PortfolioSummaryResponse> summaryList = new ArrayList<>();

        for (Holdings holding : holdingsList) {

            PortfolioSummaryResponse summary = new PortfolioSummaryResponse();

            Double latestPrice =
                priceSnapshotsRepository
                    .findByAssetSymbolOrderByCapturedAtDesc(
                        holding.getAssetSymbol())
                    .stream()
                    .findFirst()
                    .map(p -> p.getPriceUsd())
                    .orElse(0.0);

            Double investedValue =
                holding.getQuantity() * holding.getAvgCost();

            Double currentValue =
                holding.getQuantity() * latestPrice;

            summary.setAssetSymbol(holding.getAssetSymbol());
            summary.setQuantity(holding.getQuantity());
            summary.setAvgCost(holding.getAvgCost());
            summary.setCurrentPrice(latestPrice);
            summary.setInvestedValue(investedValue);
            summary.setCurrentValue(currentValue);
            summary.setProfitOrLoss(currentValue - investedValue);

            summaryList.add(summary);
        }

        return summaryList;
    }
}
