package com.cryptotracker.backend.alerts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    // ✅ Create Alert
    public Alerts createAlert(Long userId, String coinId, double price, AlertType type) {
        Alerts alert = new Alerts();
        alert.setUserId(userId);
        alert.setCoinId(coinId);
        alert.setTargetPriceInr(price);
        alert.setType(type);
        alert.setCreatedAt(LocalDateTime.now());

        return repository.save(alert);
    }

    // ✅ Get alerts for user
    public List<Alerts> getUserAlerts(Long userId) {
        return repository.findByUserId(userId);
    }

    // ✅ Delete alert
    public void deleteAlert(Long alertId, Long userId) {
        Alerts alert = repository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        if (!alert.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        repository.delete(alert);
    }

    // ✅ CRON / Manual price check
    public void checkAlerts() {

        List<Alerts> alerts = repository.findByTriggeredFalse();
        if (alerts.isEmpty()) return;

        String ids = alerts.stream()
                .map(Alerts::getCoinId)
                .distinct()
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        String url =
            "https://api.coingecko.com/api/v3/simple/price" +
            "?ids=" + ids +
            "&vs_currencies=inr";

        Map<String, Map<String, Double>> prices =
                restTemplate.getForObject(url, Map.class);

        for (Alerts alert : alerts) {
            double currentPrice =
                    prices.get(alert.getCoinId()).get("inr");

            boolean shouldTrigger =
                    (alert.getType() == AlertType.ABOVE && currentPrice >= alert.getTargetPriceInr()) ||
                    (alert.getType() == AlertType.BELOW && currentPrice <= alert.getTargetPriceInr());

            if (shouldTrigger) {
                alert.setTriggered(true);
                repository.save(alert);
            }
        }
    }
}
