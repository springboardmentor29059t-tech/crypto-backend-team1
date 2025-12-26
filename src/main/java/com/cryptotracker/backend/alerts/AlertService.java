package com.cryptotracker.backend.alerts;

import com.cryptotracker.backend.pricing.PriceSnapshot;
import com.cryptotracker.backend.pricing.PriceSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final PriceSnapshotRepository priceSnapshotRepository;

    // ✅ Create Alert
    public Alerts createAlert(
            Long userId,
            String coinId,
            double targetPrice,
            AlertType type
    ) {
        Alerts alert = new Alerts();
        alert.setUserId(userId);
        alert.setCoinId(coinId);
        alert.setTargetPriceInr(targetPrice);
        alert.setType(type);
        alert.setTriggered(false);
        alert.setCreatedAt(LocalDateTime.now());

        return alertRepository.save(alert);
    }

    // ✅ Get alerts for user
    public List<Alerts> getUserAlerts(Long userId) {
        return alertRepository.findByUserId(userId);
    }

    // ✅ Delete alert
    public void deleteAlert(Long alertId, Long userId) {
        Alerts alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        if (!alert.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        alertRepository.delete(alert);
    }

    // ✅ CORE LOGIC: Trigger alerts using DB prices
    public void checkAlerts() {

        List<Alerts> pendingAlerts =
                alertRepository.findByTriggeredFalse();

        if (pendingAlerts.isEmpty()) return;

        for (Alerts alert : pendingAlerts) {

            // 🔹 Map coinId → asset symbol
            String assetSymbol = mapCoinIdToSymbol(alert.getCoinId());

            // 🔹 Fetch latest price snapshot
            PriceSnapshot latestSnapshot =
                    priceSnapshotRepository
                            .findTopByAssetSymbolOrderByCapturedAtDesc(assetSymbol);

            if (latestSnapshot == null) continue;

            double currentPrice = latestSnapshot.getPriceInr();

            boolean shouldTrigger =
                    (alert.getType() == AlertType.ABOVE &&
                            currentPrice >= alert.getTargetPriceInr())
                 || (alert.getType() == AlertType.BELOW &&
                            currentPrice <= alert.getTargetPriceInr());

            if (shouldTrigger) {
                alert.setTriggered(true);
                alertRepository.save(alert);
            }
        }
    }

    // 🔹 Helper: CoinGecko ID → Asset Symbol
    private String mapCoinIdToSymbol(String coinId) {
        return switch (coinId) {
            case "bitcoin" -> "BTC";
            case "ethereum" -> "ETH";
            case "solana" -> "SOL";
            case "binancecoin" -> "BNB";
            default -> coinId.toUpperCase();
        };
    }
}
