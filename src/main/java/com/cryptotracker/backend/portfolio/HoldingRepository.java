package com.cryptotracker.backend.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<Holding, Long> {

    // ✅ Holding has userId field
    Optional<Holding> findByUserIdAndAsset(Long userId, String asset);

    // ✅ Holding has userId field
    List<Holding> findByUserId(Long userId);
}
