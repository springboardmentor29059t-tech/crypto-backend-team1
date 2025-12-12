package com.crypto.backend.market;

import java.math.BigDecimal;
import java.util.Map;

public interface ExchangeConnector {
    // Returns Map of Asset -> Amount (e.g. "BTC" -> 0.05)
    Map<String, BigDecimal> getBalances(String apiKey, String apiSecret);
}
