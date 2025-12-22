package com.cryptotracker.backend.pricing;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceSnapshotController {

    private final PriceSnapshotService service;

    @PostMapping("/capture")
    public String capturePrices() {
        service.capturePrices();
        return "Price snapshot captured successfully";
    }
}
