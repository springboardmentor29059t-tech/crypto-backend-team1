package com.crypto.backend.risk;

import com.crypto.backend.portfolio.Holding;
import com.crypto.backend.portfolio.HoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RiskService {

    @Autowired private ScamTokenRepository scamTokenRepository;
    @Autowired private RiskAlertRepository riskAlertRepository;
    @Autowired private HoldingRepository holdingRepository;

    // 1. Seed Method (Simulating data from CryptoScamDB)
    public void seedScamData() {
        if (scamTokenRepository.count() == 0) {
            // Adding sample scam tokens based on your doc schema
            scamTokenRepository.save(new ScamToken("0x1234567890abcdef", "ETH", "high", "CryptoScamDB"));
            scamTokenRepository.save(new ScamToken("0xdeadbeefdeadbeef", "BSC", "medium", "Manual Report"));
            System.out.println("Scam data seeded.");
        }
    }

    // 2. Scan Portfolio Logic
    public List<RiskAlert> scanPortfolio(Long userId) {
        List<Holding> holdings = holdingRepository.findByUserId(userId);

        for (Holding h : holdings) {
            // Check if holding has an address (wallet holdings)
            if (h.getAddress() != null) {
                var scamOpt = scamTokenRepository.findByContractAddress(h.getAddress());

                if (scamOpt.isPresent()) {
                    ScamToken scam = scamOpt.get();
                    saveAlert(userId, h.getAssetSymbol(), "contract_risk",
                            "Scam Detected! Contract " + scam.getContractAddress() +
                                    " is flagged as " + scam.getRiskLevel() + " risk by " + scam.getSource());
                }
            }

            // Also check by Symbol (Simple check for known bad symbols like 'SQUID')
            // In a real app, you'd map Symbol -> Contract first.
            // Here we assume if the user holds a coin named "SQUID", it's risky.
            if ("SQUID".equalsIgnoreCase(h.getAssetSymbol())) {
                saveAlert(userId, h.getAssetSymbol(), "rugpull_warning",
                        "Warning: This token symbol has been associated with rugpulls.");
            }
        }
        return riskAlertRepository.findByUserId(userId);
    }

    private void saveAlert(Long userId, String symbol, String type, String details) {
        // Avoid duplicate alerts (optional logic)
        RiskAlert alert = new RiskAlert(userId, symbol, type, details);
        riskAlertRepository.save(alert);
    }

    public List<RiskAlert> getAlerts(Long userId) {
        return riskAlertRepository.findByUserId(userId);
    }
}
