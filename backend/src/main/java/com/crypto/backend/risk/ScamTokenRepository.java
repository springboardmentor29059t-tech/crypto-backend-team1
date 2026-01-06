package com.crypto.backend.risk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ScamTokenRepository extends JpaRepository<ScamToken, Long> {
    // Find by contract address (case-insensitive usually handled by DB or UpperCase in code)
    Optional<ScamToken> findByContractAddress(String contractAddress);
}
