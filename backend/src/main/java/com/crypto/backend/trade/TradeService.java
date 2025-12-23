package com.crypto.backend.trade;

import com.crypto.backend.exchange.Exchange;
import com.crypto.backend.exchange.ExchangeRepository;
import com.crypto.backend.user.User;
import com.crypto.backend.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final ExchangeRepository exchangeRepository;

    public TradeService(TradeRepository tradeRepository,
                        UserRepository userRepository,
                        ExchangeRepository exchangeRepository) {
        this.tradeRepository = tradeRepository;
        this.userRepository = userRepository;
        this.exchangeRepository = exchangeRepository;
    }

    public List<Trade> getTrades(Long userId) {
        return tradeRepository.findByUserId(userId);
    }

    @Transactional
    public Trade addManualTrade(Long userId,
                                Long exchangeId,
                                String assetSymbol,
                                String side,
                                BigDecimal quantity,
                                BigDecimal price,
                                BigDecimal fee,
                                LocalDateTime executedAt) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        Trade trade = new Trade();
        trade.setUser(user);
        trade.setExchange(exchange);
        trade.setAssetSymbol(assetSymbol);
        trade.setSide(side);            // "BUY" or "SELL"
        trade.setQuantity(quantity);
        trade.setPrice(price);
        trade.setFee(fee);
        trade.setExecutedAt(executedAt != null ? executedAt : LocalDateTime.now());

        return tradeRepository.save(trade);
    }

    // --- NEW: compute average cost for a user + asset from BUY trades only ---
    public BigDecimal computeAverageCost(Long userId, String assetSymbol) {
        List<Trade> trades = tradeRepository
                .findByUserIdAndAssetSymbolOrderByExecutedAtAsc(userId, assetSymbol);

        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalQty  = BigDecimal.ZERO;

        for (Trade trade : trades) {
            if (!"BUY".equalsIgnoreCase(trade.getSide())) {
                continue; // ignore SELL for cost basis
            }

            BigDecimal qty = trade.getQuantity();
            BigDecimal price = trade.getPrice();
            BigDecimal fee = trade.getFee() != null ? trade.getFee() : BigDecimal.ZERO;

            // cost = qty * price + fee
            BigDecimal cost = qty.multiply(price).add(fee);

            totalCost = totalCost.add(cost);
            totalQty = totalQty.add(qty);
        }

        if (totalQty.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO; // no BUY trades yet
        }

        // avgCost = totalCost / totalQty  (8 decimal places)
        return totalCost.divide(totalQty, 8, java.math.RoundingMode.HALF_UP);
    }
}
