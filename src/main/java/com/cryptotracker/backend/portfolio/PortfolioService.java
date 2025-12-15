package com.cryptotracker.backend.portfolio;

import com.cryptotracker.backend.transactions.Transaction;
import com.cryptotracker.backend.transactions.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final TransactionRepository transactionRepository;

    public List<PortfolioHoldingDto> getUserPortfolio(Long userId) {

        List<Transaction> transactions =
                transactionRepository.findByUserId(userId);

        Map<String, PortfolioHoldingDto> map = new HashMap<>();

        for (Transaction tx : transactions) {

            String asset = tx.getAsset();
            map.putIfAbsent(asset, new PortfolioHoldingDto(asset, 0, 0));

            PortfolioHoldingDto holding = map.get(asset);

            if ("BUY".equals(tx.getType())) {
                double totalCost =
                        holding.getAvgBuyPrice() * holding.getQuantity()
                        + tx.getPrice() * tx.getQuantity();

                double newQty = holding.getQuantity() + tx.getQuantity();

                holding.setAvgBuyPrice(
                        newQty == 0 ? 0 : totalCost / newQty
                );
                holding.setQuantity(newQty);
            }

            if ("SELL".equals(tx.getType())) {
                holding.setQuantity(
                        holding.getQuantity() - tx.getQuantity()
                );
            }
        }

        return new ArrayList<>(map.values());
    }
}
