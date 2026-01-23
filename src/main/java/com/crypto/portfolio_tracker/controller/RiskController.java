package com.crypto.portfolio_tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.portfolio_tracker.model.RiskAlerts;
import com.crypto.portfolio_tracker.model.ScamTokens;
import com.crypto.portfolio_tracker.repository.RiskAlertsRepository;
import com.crypto.portfolio_tracker.service.RiskCheckService;

@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final RiskCheckService riskCheckService;
    private final RiskAlertsRepository riskAlertsRepository;

    public RiskController(RiskCheckService riskCheckService,
                          RiskAlertsRepository riskAlertsRepository) {
        this.riskCheckService = riskCheckService;
        this.riskAlertsRepository = riskAlertsRepository;
    }

    @PostMapping("/check")
    public ResponseEntity<ScamTokens> checkRisk(
            @RequestParam Long userId,
            @RequestParam String assetSymbol,
            @RequestParam String contractAddress,
            @RequestParam String chain) {

        ScamTokens token =
                riskCheckService.checkTokenRisk(contractAddress, chain);

        // Create alert if high risk
        if ("high".equalsIgnoreCase(token.getRiskLevel())) {
            RiskAlerts alert = new RiskAlerts();
            alert.setUserId(userId);
            alert.setAssetSymbol(assetSymbol);
            alert.setAlertType("contract_risk");
            alert.setDetails("High risk or scam token detected");

            riskAlertsRepository.save(alert);
        }

        return ResponseEntity.ok(token);
    }
}
