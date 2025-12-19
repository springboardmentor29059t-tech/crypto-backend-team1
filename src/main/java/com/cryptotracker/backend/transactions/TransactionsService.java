package com.cryptotracker.backend.transactions;

import com.cryptotracker.backend.portfolio.Holding;
import com.cryptotracker.backend.portfolio.HoldingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionsService {

    private final TransactionRepository transactionRepository;
    private final HoldingRepository holdingRepository;

    public List<Transaction> getTransactions(Long userId) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Transaction addTransaction(Transaction tx, Long userId) {

        tx.setUserId(userId);
        tx.setCreatedAt(LocalDateTime.now());

        Holding holding = holdingRepository
                .findByUserIdAndAsset(userId, tx.getAsset())
                .orElse(null);

        if (tx.getType().equals("BUY")) {
            handleBuy(tx, holding, userId);
        } else {
            handleSell(tx, holding);
        }

        return transactionRepository.save(tx);
    }

    private void handleBuy(Transaction tx, Holding holding, Long userId) {

        if (holding == null) {
            holding = new Holding();
            holding.setUserId(userId);
            holding.setAsset(tx.getAsset());
            holding.setQuantity(tx.getQuantity());
            holding.setAvgBuyPrice(tx.getPrice());
        } else {
            double totalCost =
                    holding.getQuantity() * holding.getAvgBuyPrice()
                            + tx.getQuantity() * tx.getPrice();

            double newQty = holding.getQuantity() + tx.getQuantity();

            holding.setQuantity(newQty);
            holding.setAvgBuyPrice(totalCost / newQty);
        }

        holdingRepository.save(holding);
    }

    private void handleSell(Transaction tx, Holding holding) {

        if (holding == null || holding.getQuantity() < tx.getQuantity()) {
            throw new RuntimeException("Insufficient balance to sell");
        }

        holding.setQuantity(
                holding.getQuantity() - tx.getQuantity()
        );

        holdingRepository.save(holding);
    }

    public void deleteTransaction(Long id, Long userId) {
        transactionRepository.deleteById(id);
    }
}
