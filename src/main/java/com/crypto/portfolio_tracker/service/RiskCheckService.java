package com.crypto.portfolio_tracker.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.crypto.portfolio_tracker.model.ScamTokens;
import com.crypto.portfolio_tracker.repository.ScamTokensRepository;

@Service
public class RiskCheckService {

    private final ScamTokensRepository scamTokensRepository;

    public RiskCheckService(ScamTokensRepository scamTokensRepository) {
        this.scamTokensRepository = scamTokensRepository;
    }

    public ScamTokens checkTokenRisk(String contractAddress, String chain) {

        Optional<ScamTokens> existing =
                scamTokensRepository.findByContractAddress(contractAddress);

        if (existing.isPresent()) {
            return existing.get();
        }

        // Simple demo logic (replace with real API later)
        ScamTokens token = new ScamTokens();
        token.setContractAddress(contractAddress);
        token.setChain(chain);
        token.setRiskLevel("low");
        token.setSource("CryptoScamDB");

        // Example: mark suspicious addresses
        if (contractAddress.toLowerCase().startsWith("0xdead")) {
            token.setRiskLevel("high");
        }

        return scamTokensRepository.save(token);
    }
}
