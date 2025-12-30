package com.cryptotracker.backend.risk;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RiskAlertService {

    private final RiskAlertRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    // 🔹 Known risky assets (simple + safe for internship)
    private static final Set<String> RISKY_ASSETS = Set.of(
            "LUNA",
            "SQUID",
            "SCAM"
    );

    // 🔹 Called AFTER transaction
    public RiskAlert checkRisk(Long userId, String asset) {

        RiskAlert alert = new RiskAlert();
        alert.setUserId(userId);
        alert.setAsset(asset);
        alert.setCreatedAt(LocalDateTime.now());

        if (isRisky(asset)) {
            alert.setRiskLevel("WARNING");
            alert.setMessage(
                    "This asset has been reported in scam databases. Trade with caution."
            );
        } else {
            alert.setRiskLevel("SAFE");
            alert.setMessage(
                    "No known risks found for this asset."
            );
        }

        return repository.save(alert);
    }

    // 🔹 Risk logic (can be extended later to APIs)
    private boolean isRisky(String asset) {
        return RISKY_ASSETS.contains(asset.toUpperCase());
    }
}
