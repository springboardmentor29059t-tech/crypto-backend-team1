package com.crypto.backend.apikey;

public class ApiKeyCreateRequest {

    private Long userId;       // Who does this key belong to?
    private Long exchangeId;   // Which exchange is this for?
    private String apiKey;
    private String apiSecret;
    private String label;      // e.g., "My Binance Main"

    // --- Getters and Setters ---
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getExchangeId() { return exchangeId; }
    public void setExchangeId(Long exchangeId) { this.exchangeId = exchangeId; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getApiSecret() { return apiSecret; }
    public void setApiSecret(String apiSecret) { this.apiSecret = apiSecret; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}
