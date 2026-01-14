package com.cryptotracker.backend.reports;

import com.cryptotracker.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pnl")
@RequiredArgsConstructor
public class PnLController {

    private final PnLService pnlService;
    private final JwtService jwtService;

    @GetMapping("/summary")
    public PnLSummaryDto getSummary(
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return pnlService.calculatePnL(userId);
    }
}
