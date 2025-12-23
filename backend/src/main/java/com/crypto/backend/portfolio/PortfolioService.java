package com.crypto.backend.portfolio;

import com.crypto.backend.exchange.Exchange;
import com.crypto.backend.exchange.ExchangeRepository;
import com.crypto.backend.trade.TradeService;
import com.crypto.backend.user.User;
import com.crypto.backend.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioService {

    private final HoldingRepository holdingRepository;
    private final UserRepository userRepository;
    private final ExchangeRepository exchangeRepository;
    private final TradeService tradeService;

    public PortfolioService(HoldingRepository holdingRepository,
                            UserRepository userRepository,
                            ExchangeRepository exchangeRepository,
                            TradeService tradeService) {
        this.holdingRepository = holdingRepository;
        this.userRepository = userRepository;
        this.exchangeRepository = exchangeRepository;
        this.tradeService = tradeService;
    }

    public List<Holding> getHoldings(Long userId) {
        return holdingRepository.findByUserId(userId);
    }

    // --- From Week 3: sync balances from exchange into holdings ---
    @Transactional
    public void syncBalances(Long userId, Long exchangeId, Map<String, BigDecimal> exchangeBalances) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        for (Map.Entry<String, BigDecimal> entry : exchangeBalances.entrySet()) {
            String symbol = entry.getKey();
            BigDecimal amount = entry.getValue();

            Holding holding = holdingRepository
                    .findByUserIdAndAssetSymbolAndExchangeId(userId, symbol, exchangeId)
                    .orElse(new Holding());

            if (holding.getId() == null) {
                holding.setUser(user);
                holding.setAssetSymbol(symbol);
                holding.setExchange(exchange);
                holding.setWalletType("EXCHANGE");
                holding.setAvgCost(BigDecimal.ZERO);
            }

            holding.setQuantity(amount);
            holding.setUpdatedAt(LocalDateTime.now());

            holdingRepository.save(holding);
        }
    }

    // --- From Week 3: manual add/edit holding ---
    @Transactional
    public Holding manualAddOrUpdate(Long userId, HoldingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Exchange exchange = null;
        if (request.getExchangeId() != null) {
            exchange = exchangeRepository.findById(request.getExchangeId())
                    .orElse(null);
        }

        Holding holding;
        if (exchange != null) {
            holding = holdingRepository
                    .findByUserIdAndAssetSymbolAndExchangeId(userId,
                            request.getAssetSymbol(),
                            request.getExchangeId())
                    .orElse(new Holding());
        } else {
            holding = new Holding();
        }

        holding.setUser(user);
        holding.setAssetSymbol(request.getAssetSymbol());
        holding.setQuantity(request.getQuantity());
        holding.setWalletType(request.getWalletType());
        holding.setExchange(exchange);
        holding.setAddress(request.getAddress());
        holding.setUpdatedAt(LocalDateTime.now());

        if (holding.getAvgCost() == null) {
            holding.setAvgCost(BigDecimal.ZERO);
        }

        return holdingRepository.save(holding);
    }

    // --- NEW: recompute avgCost for all holdings of a user from trades ---
    @Transactional
    public void updateAverageCostForUser(Long userId) {
        List<Holding> holdings = holdingRepository.findByUserId(userId);

        for (Holding holding : holdings) {
            String symbol = holding.getAssetSymbol();

            BigDecimal avgCost = tradeService.computeAverageCost(userId, symbol);

            holding.setAvgCost(avgCost);
            holding.setUpdatedAt(LocalDateTime.now());
            holdingRepository.save(holding);
        }
    }
}
