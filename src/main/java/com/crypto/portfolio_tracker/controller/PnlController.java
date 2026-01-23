package com.crypto.portfolio_tracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.portfolio_tracker.dto.PnlResponse;
import com.crypto.portfolio_tracker.service.PnlService;

@RestController
@RequestMapping("/api/pnl")
public class PnlController {

    private final PnlService pnlService;

    public PnlController(PnlService pnlService) {
        this.pnlService = pnlService;
    }

    // View P&L summary
    @GetMapping("/summary/{userId}")
    public ResponseEntity<List<PnlResponse>> getPnlSummary(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
            pnlService.calculateUnrealizedPnl(userId)
        );
    }
}
