package com.crypto.portfolio_tracker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.crypto.portfolio_tracker.dto.PnlResponse;
import com.crypto.portfolio_tracker.model.Holdings;
import com.crypto.portfolio_tracker.repository.HoldingsRepository;
import com.crypto.portfolio_tracker.repository.PriceSnapshotsRepository;

@Service
public class PnlService {

    private final HoldingsRepository holdingsRepository;
    private final PriceSnapshotsRepository priceSnapshotsRepository;

    public PnlService(HoldingsRepository holdingsRepository,
                      PriceSnapshotsRepository priceSnapshotsRepository) {
        this.holdingsRepository = holdingsRepository;
        this.priceSnapshotsRepository = priceSnapshotsRepository;
    }

    public List<PnlResponse> calculateUnrealizedPnl(Long userId) {

        List<Holdings> holdings = holdingsRepository.findByUserId(userId);
        List<PnlResponse> pnlList = new ArrayList<>();

        for (Holdings h : holdings) {

            Double latestPrice =
                priceSnapshotsRepository
                    .findByAssetSymbolOrderByCapturedAtDesc(h.getAssetSymbol())
                    .stream()
                    .findFirst()
                    .map(p -> p.getPriceUsd())
                    .orElse(0.0);

            Double investedValue = h.getQuantity() * h.getAvgCost();
            Double currentValue = h.getQuantity() * latestPrice;

            PnlResponse response = new PnlResponse();
            response.setAssetSymbol(h.getAssetSymbol());
            response.setQuantity(h.getQuantity());
            response.setAvgCost(h.getAvgCost());
            response.setCurrentPrice(latestPrice);
            response.setInvestedValue(investedValue);
            response.setCurrentValue(currentValue);
            response.setUnrealizedPnl(currentValue - investedValue);

            pnlList.add(response);
        }

        return pnlList;
    }
}
