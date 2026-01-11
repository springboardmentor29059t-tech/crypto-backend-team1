package com.cryptotracker.backend.dashboard;

import com.cryptotracker.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final JwtService jwtService;

    @GetMapping("/summary")
    public DashboardSummaryDto getSummary(
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return dashboardService.getSummary(userId);
    }
}
