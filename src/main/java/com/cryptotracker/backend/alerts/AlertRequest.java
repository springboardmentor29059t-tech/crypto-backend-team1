package com.cryptotracker.backend.alerts;

public record AlertRequest(
        String coinId,
        double price,
        AlertType type
) {}
