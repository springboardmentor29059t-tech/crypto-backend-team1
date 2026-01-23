package com.crypto.portfolio_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.portfolio_tracker.model.Holdings;

public interface HoldingsRepository extends JpaRepository<Holdings, Long> {

    List<Holdings> findByUserId(Long userId);
}
