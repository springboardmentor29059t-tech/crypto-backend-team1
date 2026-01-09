package com.crypto.backend.risk;

import com.crypto.backend.portfolio.Holding;
import com.crypto.backend.portfolio.HoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RiskService {

    @Autowired private ScamTokenRepository scamTokenRepository;
    @Autowired private RiskAlertRepository riskAlertRepository;
    @Autowired private HoldingRepository holdingRepository;

    // 1. Seed Method
    public void seedScamData() {
        if (scamTokenRepository.count() == 0) {
            // FIX: Using Constructor instead of Setters
            // Constructor signature from ScamToken.java:
            // new ScamToken(contractAddress, chain, riskLevel, source)

            // Note: If you added a 'symbol' field to ScamToken entity, make sure to update the Entity constructor too.
            // Assuming for now we stick to the EXISTING constructor and rely on logic hacks or separate setters if available.

            // Since the error says "cannot find setContractAddress", it implies you only have Getters.
            // So we MUST use the constructor you defined:
            // public ScamToken(String contractAddress, String chain, String riskLevel, String source)

            ScamToken scam1 = new ScamToken(
                    "0x1234567890abcdef",
                    "ETH",
                    "high",
                    "CryptoScamDB"
            );
            // If you added 'symbol' to the Entity, use: scam1.setSymbol("FAKE"); if setter exists, otherwise logic needs Entity update.
            // Assuming you added the 'symbol' field + Setters as requested in previous step.
            // If NOT, this line below will fail too. ENSURE ScamToken.java has setSymbol or add it to constructor.
            scam1.setSymbol("FAKE");

            scamTokenRepository.save(scam1);

            ScamToken scam2 = new ScamToken(
                    "0x0000000000000000",
                    "BSC",
                    "high",
                    "Manual Report"
            );
            scam2.setSymbol("SQUID"); // Identify this as the SQUID scam
            scamTokenRepository.save(scam2);

            System.out.println("Scam data seeded.");
        }
    }

    // 2. Scan Portfolio Logic
    public List<RiskAlert> scanPortfolio(Long userId) {
        List<Holding> holdings = holdingRepository.findByUserId(userId);

        for (Holding h : holdings) {
            // Check 1: By Contract Address
            if (h.getAddress() != null) {
                var scamOpt = scamTokenRepository.findByContractAddress(h.getAddress());
                if (scamOpt.isPresent()) {
                    ScamToken scam = scamOpt.get();
                    saveAlert(userId, h.getAssetSymbol(), "contract_risk",
                            "Scam Detected! Contract " + scam.getContractAddress() +
                                    " is flagged as " + scam.getRiskLevel() + " risk by " + scam.getSource());
                }
            }

            // Check 2: By Symbol (Dynamic DB Check)
            if (h.getAssetSymbol() != null) {
                Optional<ScamToken> symbolScam = scamTokenRepository.findBySymbol(h.getAssetSymbol().toUpperCase());

                if (symbolScam.isPresent()) {
                    ScamToken scam = symbolScam.get();
                    saveAlert(userId, h.getAssetSymbol(), "rugpull_warning",
                            "Warning: Token " + h.getAssetSymbol() +
                                    " is flagged as " + scam.getRiskLevel() + " risk (Source: " + scam.getSource() + ")");
                }
            }
        }
        return riskAlertRepository.findByUserId(userId);
    }

    private void saveAlert(Long userId, String symbol, String type, String details) {
        RiskAlert alert = new RiskAlert(userId, symbol, type, details);
        riskAlertRepository.save(alert);
    }

    public List<RiskAlert> getAlerts(Long userId) {
        return riskAlertRepository.findByUserId(userId);
    }
}
