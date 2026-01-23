package com.crypto.portfolio_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.portfolio_tracker.model.RiskAlerts;

public interface RiskAlertsRepository extends JpaRepository<RiskAlerts, Long> {
}
