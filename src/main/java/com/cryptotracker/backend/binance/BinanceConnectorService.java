package com.cryptotracker.backend.binance;

import org.springframework.stereotype.Service;

@Service
public class BinanceConnectorService {

    // Placeholder. Later implement signing and API calls using saved API keys.
    public boolean testConnection(String apiKey, String apiSecret) {
        // For now just return true if both not blank
        return apiKey != null && !apiKey.isBlank() && apiSecret != null && !apiSecret.isBlank();
    }
}
