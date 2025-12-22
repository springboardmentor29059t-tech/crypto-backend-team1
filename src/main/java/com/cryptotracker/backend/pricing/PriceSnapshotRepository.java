package com.cryptotracker.backend.pricing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceSnapshotRepository
        extends JpaRepository<PriceSnapshot, Long> {
}
