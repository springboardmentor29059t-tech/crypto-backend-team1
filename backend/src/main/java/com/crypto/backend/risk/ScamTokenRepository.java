package com.crypto.backend.risk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ScamTokenRepository extends JpaRepository<ScamToken, Long> {

    // Find by contract address
    Optional<ScamToken> findByContractAddress(String contractAddress);

    // NEW: Find by token symbol (e.g. "SQUID", "FTT")
    Optional<ScamToken> findBySymbol(String symbol);
}
