package com.crypto.portfolio_tracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.portfolio_tracker.dto.TaxSummaryResponse;
import com.crypto.portfolio_tracker.service.PnlService;
import com.crypto.portfolio_tracker.service.TaxService;

@RestController
@RequestMapping("/api/tax")
public class TaxController {

    private final PnlService pnlService;
    private final TaxService taxService;

    public TaxController(PnlService pnlService, TaxService taxService) {
        this.pnlService = pnlService;
        this.taxService = taxService;
    }

    @GetMapping("/summary/{userId}")
    public ResponseEntity<List<TaxSummaryResponse>> getTaxSummary(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
            taxService.prepareTaxSummary(
                pnlService.calculateUnrealizedPnl(userId)
            )
        );
    }
}
