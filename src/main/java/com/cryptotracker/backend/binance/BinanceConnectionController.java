package com.cryptotracker.backend.binance;

import com.cryptotracker.backend.exchange.UserApiKey;
import com.cryptotracker.backend.exchange.UserApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/binance")
@RequiredArgsConstructor
public class BinanceConnectionController {

    private final BinanceConnectorService binanceService;
    private final UserApiKeyRepository userApiKeyRepository;

    @GetMapping("/test/{keyId}")
    public boolean testConnection(@PathVariable Long keyId) {

        UserApiKey key = userApiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("API Key not found"));

        return binanceService.testConnection(key.getApiKey(), key.getApiSecret());
    }
}
