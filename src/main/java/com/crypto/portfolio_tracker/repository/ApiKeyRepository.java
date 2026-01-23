package com.crypto.portfolio_tracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.portfolio_tracker.model.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByUserIdAndExchangeId(Long userId, Long exchangeId);
}
