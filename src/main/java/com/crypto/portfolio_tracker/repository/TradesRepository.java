package com.crypto.portfolio_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.portfolio_tracker.model.Trades;

public interface TradesRepository extends JpaRepository<Trades, Long> {

    List<Trades> findByUserId(Long userId);
}
