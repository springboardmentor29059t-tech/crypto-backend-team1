package com.crypto.portfolio_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.portfolio_tracker.model.PriceSnapshots;

public interface PriceSnapshotsRepository extends JpaRepository<PriceSnapshots, Long> {

    List<PriceSnapshots> findByAssetSymbolOrderByCapturedAtDesc(String assetSymbol);
}
