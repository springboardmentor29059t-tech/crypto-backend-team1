package com.cryptotracker.backend.dashboard;

import com.cryptotracker.backend.portfolio.HoldingDto;
import com.cryptotracker.backend.portfolio.PortfolioService;
import com.cryptotracker.backend.pricing.PriceSnapshot;
import com.cryptotracker.backend.pricing.PriceSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PortfolioService portfolioService;
    private final PriceSnapshotRepository priceSnapshotRepository;

    public DashboardSummaryDto getSummary(Long userId) {

        List<HoldingDto> holdings =
                portfolioService.getUserHoldings(userId);

        double totalValue = 0;
        double totalPL = 0;

        String bestAsset = null;
        double bestPct = Double.NEGATIVE_INFINITY;

        for (HoldingDto h : holdings) {

            PriceSnapshot snapshot =
                    priceSnapshotRepository
                            .findTopByAssetSymbolOrderByCapturedAtDesc(h.getAsset());

            if (snapshot == null) continue;

            double qty = h.getQuantity();
            double avgBuy = h.getAvgBuyPrice();
            double currentPrice = snapshot.getPriceInr();

            double invested = qty * avgBuy;
            double current = qty * currentPrice;
            double pnl = current - invested;

            totalValue += current;
            totalPL += pnl;

            double pct = invested == 0 ? 0 : (pnl / invested) * 100;
            if (pct > bestPct) {
                bestPct = pct;
                bestAsset = h.getAsset();
            }
        }

        return new DashboardSummaryDto(
                totalValue,
                totalPL,
                bestAsset,
                holdings.size()
        );
    }
}
