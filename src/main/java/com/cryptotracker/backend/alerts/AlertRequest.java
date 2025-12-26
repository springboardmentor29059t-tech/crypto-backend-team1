package com.cryptotracker.backend.alerts;

public record AlertRequest(
        String coinId,          // bitcoin, ethereum, solana
        double price,           // target price in INR
        AlertType type           // ABOVE / BELOW
) {}
