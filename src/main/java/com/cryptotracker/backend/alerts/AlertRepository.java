package com.cryptotracker.backend.alerts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alerts, Long> {

    List<Alerts> findByUserId(Long userId);

    List<Alerts> findByTriggeredFalse();
}
