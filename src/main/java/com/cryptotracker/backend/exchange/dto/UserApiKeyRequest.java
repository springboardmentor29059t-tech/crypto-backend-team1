package com.cryptotracker.backend.exchange.dto;

import lombok.Data;

@Data
public class UserApiKeyRequest {
    private Long userId;
    private Long exchangeId;
    private String apiKey;
    private String apiSecret;
    private String label;
}
