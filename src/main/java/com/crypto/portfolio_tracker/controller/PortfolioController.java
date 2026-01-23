package com.crypto.portfolio_tracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.portfolio_tracker.dto.PortfolioSummaryResponse;
import com.crypto.portfolio_tracker.service.PortfolioService;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/summary/{userId}")
    public ResponseEntity<List<PortfolioSummaryResponse>> getPortfolioSummary(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
            portfolioService.getPortfolioSummary(userId)
        );
    }
}
