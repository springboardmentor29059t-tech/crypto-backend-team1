package com.cryptotracker.backend.portfolio;

import com.cryptotracker.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final JwtService jwtService;

    // 🔹 Get user holdings (for Portfolio & Analytics pages)
    @GetMapping("/holdings")
    public List<HoldingDto> getHoldings(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return portfolioService.getUserHoldings(userId);
    }
}
