package com.cryptotracker.backend.alerts;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertScheduler {

    private final AlertService alertService;

    // 🔔 Check alerts every 1 minute
    @Scheduled(fixedRate = 60000)
    public void runAlertCheck() {
        alertService.checkAlerts();
    }
}
