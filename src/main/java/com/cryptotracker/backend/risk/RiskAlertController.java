package com.cryptotracker.backend.risk;

import com.cryptotracker.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-alerts")
@RequiredArgsConstructor
public class RiskAlertController {

    private final RiskAlertRepository repository;
    private final JwtService jwtService;

    // 🔹 Fetch user risk alerts
    @GetMapping
    public List<RiskAlert> getUserAlerts(
            @RequestHeader("Authorization") String auth
    ) {
        Long userId = extractUserId(auth);
        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    private Long extractUserId(String auth) {
        String token = auth.replace("Bearer ", "");
        return jwtService.extractUserId(token);
    }
}
