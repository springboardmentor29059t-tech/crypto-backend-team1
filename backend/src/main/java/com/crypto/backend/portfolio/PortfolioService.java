package com.crypto.backend.portfolio;

import com.crypto.backend.exchange.Exchange;
import com.crypto.backend.exchange.ExchangeRepository;
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

    public PortfolioService(HoldingRepository holdingRepository,
                            UserRepository userRepository,
                            ExchangeRepository exchangeRepository) {
        this.holdingRepository = holdingRepository;
        this.userRepository = userRepository;
        this.exchangeRepository = exchangeRepository;
    }

    public List<Holding> getHoldings(Long userId) {
        return holdingRepository.findByUserId(userId);
    }

    @Transactional
    public void syncBalances(Long userId, Long exchangeId, Map<String, BigDecimal> exchangeBalances) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        for (Map.Entry<String, BigDecimal> entry : exchangeBalances.entrySet()) {
            String symbol = entry.getKey();
            BigDecimal amount = entry.getValue();

            // Check if holding exists FOR THIS SPECIFIC EXCHANGE
            Holding holding = holdingRepository.findByUserIdAndAssetSymbolAndExchangeId(userId, symbol, exchangeId)
                    .orElse(new Holding());

            if (holding.getId() == null) {
                // Set fields only for new rows
                holding.setUser(user);
                holding.setAssetSymbol(symbol);
                holding.setExchange(exchange);
                holding.setWalletType("EXCHANGE");
                // avgCost is 0 for now until Week 4
                holding.setAvgCost(BigDecimal.ZERO);
            }

            // Always update these
            holding.setQuantity(amount);
            holding.setUpdatedAt(LocalDateTime.now());

            holdingRepository.save(holding);
        }
    }

    // --- NEW METHOD FOR MANUAL ADD ---
    @Transactional
    public Holding manualAddOrUpdate(Long userId, HoldingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1. Determine the Exchange (if any)
        Exchange exchange = null;
        if (request.getExchangeId() != null) {
            exchange = exchangeRepository.findById(request.getExchangeId())
                    .orElse(null);
        }

        // 2. Determine if we are updating an existing Exchange holding or creating a new Wallet one
        Holding holding;
        if (exchange != null) {
            // It's an exchange holding: Try to find it
            holding = holdingRepository.findByUserIdAndAssetSymbolAndExchangeId(userId, request.getAssetSymbol(), request.getExchangeId())
                    .orElse(new Holding());
        } else {
            // It's a personal wallet: For now, always create new (Simplification for Week 3)
            // Ideally, we would look up by Wallet Address + Symbol, but let's keep it simple.
            holding = new Holding();
        }

        // 3. Set Values
        holding.setUser(user);
        holding.setAssetSymbol(request.getAssetSymbol());
        holding.setQuantity(request.getQuantity());
        holding.setWalletType(request.getWalletType()); // "WALLET" or "EXCHANGE"
        holding.setExchange(exchange);
        holding.setAddress(request.getAddress());
        holding.setUpdatedAt(LocalDateTime.now());

        // Default avgCost to 0 if not set
        if (holding.getAvgCost() == null) {
            holding.setAvgCost(BigDecimal.ZERO);
        }

        return holdingRepository.save(holding);
    }
}
