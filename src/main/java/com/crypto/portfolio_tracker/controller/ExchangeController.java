package com.crypto.portfolio_tracker.controller;

import com.crypto.portfolio_tracker.dto.ConnectExchangeRequest;
import com.crypto.portfolio_tracker.model.ApiKey;
import com.crypto.portfolio_tracker.model.Exchange;
import com.crypto.portfolio_tracker.repository.ApiKeyRepository;
import com.crypto.portfolio_tracker.repository.ExchangeRepository;
import com.crypto.portfolio_tracker.service.EncryptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/exchanges")
public class ExchangeController {

    private final ExchangeRepository exchangeRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final EncryptionService encryptionService;

    public ExchangeController(ExchangeRepository exchangeRepository,
                              ApiKeyRepository apiKeyRepository,
                              EncryptionService encryptionService) {
        this.exchangeRepository = exchangeRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/connect")
    public ResponseEntity<String> connectExchange(@RequestBody ConnectExchangeRequest request) {

        // 1. Find or create exchange row
        Exchange exchange = exchangeRepository
                .findByName(request.getExchangeName())
                .orElseGet(() -> {
                    Exchange newExchange = new Exchange();
                    newExchange.setName(request.getExchangeName());
                    newExchange.setBaseUrl(request.getBaseUrl());
                    newExchange.setCreatedAt(LocalDateTime.now());
                    return exchangeRepository.save(newExchange);
                });

        // 2. Encrypt API key + secret
        String encryptedKey = encryptionService.encrypt(request.getApiKey());
        String encryptedSecret = encryptionService.encrypt(request.getApiSecret());

        // 3. Save into ApiKeys table
        ApiKey apiKey = new ApiKey();
        apiKey.setUserId(request.getUserId());
        apiKey.setExchangeId(exchange.getId());
        apiKey.setApiKey(encryptedKey);
        apiKey.setApiSecret(encryptedSecret);
        apiKey.setLabel(request.getLabel());
        apiKey.setCreatedAt(LocalDateTime.now());

        apiKeyRepository.save(apiKey);

        return ResponseEntity.ok("Exchange connected and API key stored securely");
    }
}
