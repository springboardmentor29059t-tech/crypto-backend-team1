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

    // 🔹 Core Logic: Build Holdings using Weighted Average Cost
    public List<HoldingDto> getUserHoldings(Long userId) {

        List<Transaction> transactions =
                transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);

        Map<String, HoldingDto> holdingsMap = new HashMap<>();

        for (Transaction tx : transactions) {
            String asset = tx.getAsset();

            holdingsMap.putIfAbsent(asset, new HoldingDto(asset, 0, 0));

            HoldingDto holding = holdingsMap.get(asset);

            if ("BUY".equalsIgnoreCase(tx.getType())) {
                applyBuy(holding, tx);
            } else if ("SELL".equalsIgnoreCase(tx.getType())) {
                applySell(holding, tx);
            }
        }

        // Remove assets with zero quantity
        holdingsMap.values().removeIf(h -> h.getQuantity() <= 0);

        return new ArrayList<>(holdingsMap.values());
    }

    // 🔹 BUY logic → Weighted Average
    private void applyBuy(HoldingDto holding, Transaction tx) {

        double totalCost =
                (holding.getQuantity() * holding.getAvgBuyPrice()) +
                (tx.getQuantity() * tx.getPrice());

        double newQuantity = holding.getQuantity() + tx.getQuantity();

        double newAvg =
                newQuantity == 0 ? 0 : totalCost / newQuantity;

        holding.setQuantity(newQuantity);
        holding.setAvgBuyPrice(newAvg);
    }

    // 🔹 SELL logic → reduce quantity, avg stays same
    private void applySell(HoldingDto holding, Transaction tx) {

        double newQuantity = holding.getQuantity() - tx.getQuantity();

        if (newQuantity < 0) {
            throw new RuntimeException(
                "Sell quantity exceeds available holdings for " + holding.getAsset()
            );
        }

        holding.setQuantity(newQuantity);

        // avgBuyPrice remains unchanged (industry standard)
    }
}
