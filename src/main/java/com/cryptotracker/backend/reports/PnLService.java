package com.cryptotracker.backend.reports;

import com.cryptotracker.backend.pricing.PriceSnapshot;
import com.cryptotracker.backend.pricing.PriceSnapshotRepository;
import com.cryptotracker.backend.transactions.Transaction;
import com.cryptotracker.backend.transactions.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PnLService {

    private final TransactionRepository transactionRepository;
    private final PriceSnapshotRepository priceSnapshotRepository;

    public PnLSummaryDto calculatePnL(Long userId) {

        List<Transaction> transactions =
                transactionRepository.findByUserIdOrderByCreatedAtAsc(userId);

        Map<String, Double> quantityMap = new HashMap<>();
        Map<String, Double> costMap = new HashMap<>();

        double realizedPnL = 0;

        for (Transaction tx : transactions) {

            quantityMap.putIfAbsent(tx.getAsset(), 0.0);
            costMap.putIfAbsent(tx.getAsset(), 0.0);

            if ("BUY".equalsIgnoreCase(tx.getType())) {

                quantityMap.put(
                        tx.getAsset(),
                        quantityMap.get(tx.getAsset()) + tx.getQuantity()
                );

                costMap.put(
                        tx.getAsset(),
                        costMap.get(tx.getAsset()) +
                                (tx.getQuantity() * tx.getPrice())
                );
            }

            if ("SELL".equalsIgnoreCase(tx.getType())) {

                double avgBuy =
                        costMap.get(tx.getAsset()) / quantityMap.get(tx.getAsset());

                realizedPnL +=
                        (tx.getPrice() - avgBuy) * tx.getQuantity();

                quantityMap.put(
                        tx.getAsset(),
                        quantityMap.get(tx.getAsset()) - tx.getQuantity()
                );

                costMap.put(
                        tx.getAsset(),
                        costMap.get(tx.getAsset()) -
                                (avgBuy * tx.getQuantity())
                );
            }
        }

        double totalInvested =
                costMap.values().stream().mapToDouble(Double::doubleValue).sum();

        double currentValue = 0;

        for (String asset : quantityMap.keySet()) {

            PriceSnapshot latest =
                    priceSnapshotRepository
                            .findTopByAssetSymbolOrderByCapturedAtDesc(asset);

            if (latest != null) {
                currentValue += quantityMap.get(asset) * latest.getPriceInr();
            }
        }

        double unrealizedPnL = currentValue - totalInvested;

        return new PnLSummaryDto(
                totalInvested,
                currentValue,
                unrealizedPnL,
                realizedPnL
        );
    }
}
