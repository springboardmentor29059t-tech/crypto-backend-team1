package com.crypto.backend.pnl;

import com.crypto.backend.market.PriceSnapshot;
import com.crypto.backend.market.PriceSnapshotRepository;
import com.crypto.backend.portfolio.Holding;
import com.crypto.backend.portfolio.HoldingRepository;
import com.crypto.backend.portfolio.Trade;
import com.crypto.backend.portfolio.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PnLService {

    @Autowired private HoldingRepository holdingRepository;
    @Autowired private TradeRepository tradeRepository;
    @Autowired private PriceSnapshotRepository priceSnapshotRepository;

    /** Week 7 Core: Calculate ALL P&L for user */
    public PnLSummary calculatePnL(Long userId) {
        // UNREALIZED: Still holding assets
        BigDecimal unrealized = calculateUnrealizedPnL(userId);

        // REALIZED: Already sold assets
        BigDecimal realized = calculateRealizedPnL(userId);

        return new PnLSummary(userId, unrealized, realized);
    }

    private BigDecimal calculateUnrealizedPnL(Long userId) {
        BigDecimal total = BigDecimal.ZERO;
        List<Holding> holdings = holdingRepository.findByUserId(userId);

        for (Holding h : holdings) {
            if (h.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal currentPrice = getLatestPrice(h.getAssetSymbol());
                BigDecimal pnl = currentPrice.subtract(h.getAvgCost())
                        .multiply(h.getQuantity());
                total = total.add(pnl);
            }
        }
        return total;
    }

    private BigDecimal calculateRealizedPnL(Long userId) {
        BigDecimal total = BigDecimal.ZERO;
        List<Trade> sells = tradeRepository.findByUserIdAndSide(userId, "sell");

        for (Trade sell : sells) {
            Holding holding = holdingRepository.findByUserIdAndAssetSymbol(
                    userId, sell.getAssetSymbol());
            if (holding != null) {
                BigDecimal pnl = sell.getPrice().subtract(holding.getAvgCost())
                        .multiply(sell.getQuantity())
                        .subtract(sell.getFee());
                total = total.add(pnl);
            }
        }
        return total;
    }

    private BigDecimal getLatestPrice(String symbol) {
        List<PriceSnapshot> latest = priceSnapshotRepository
                .findFirstByAssetSymbolOrderByCapturedAtDesc(symbol);
        return latest.isEmpty() ? BigDecimal.valueOf(95000) : latest.get(0).getPriceUsd();
    }
}
