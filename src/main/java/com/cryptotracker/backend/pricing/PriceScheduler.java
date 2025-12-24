package com.cryptotracker.backend.pricing;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceScheduler {

    private final PriceSnapshotService priceSnapshotService;

    
    @Scheduled(cron = "0 */10 * * * *")
    public void capturePricesAutomatically() {
        System.out.println("Scheduled price capture started");
        priceSnapshotService.capturePrices();
    }
}
