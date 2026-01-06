package com.crypto.backend.risk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api") // Note: Project doc uses /api/alerts/... structure
public class RiskController {

    @Autowired private RiskService riskService;

    // Helper to seed DB
    @PostMapping("/risk/seed")
    public String seed() {
        riskService.seedScamData();
        return "Scam Database Seeded";
    }

    // Requirement: POST /api/alerts/scan/{userId} - Manual scan trigger
    @PostMapping("/alerts/scan/{userId}")
    public List<RiskAlert> triggerScan(@PathVariable Long userId) {
        return riskService.scanPortfolio(userId);
    }

    // Requirement: GET /api/alerts/{userId} - List all risk alerts
    @GetMapping("/alerts/{userId}")
    public List<RiskAlert> getUserAlerts(@PathVariable Long userId) {
        return riskService.getAlerts(userId);
    }
}
