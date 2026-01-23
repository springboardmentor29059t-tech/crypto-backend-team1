package com.crypto.portfolio_tracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.portfolio_tracker.model.ScamTokens;

public interface ScamTokensRepository extends JpaRepository<ScamTokens, Long> {

    Optional<ScamTokens> findByContractAddress(String contractAddress);
}
