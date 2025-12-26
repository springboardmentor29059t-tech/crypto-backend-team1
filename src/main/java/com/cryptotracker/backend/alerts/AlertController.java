package com.cryptotracker.backend.alerts;

import com.cryptotracker.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;
    private final JwtService jwtService;

    // ✅ Create alert
    @PostMapping
    public Alerts createAlert(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AlertRequest request
    ) {
        Long userId = extractUserId(authHeader);
        return alertService.createAlert(
                userId,
                request.coinId(),
                request.price(),
                request.type()
        );
    }

    // ✅ Get user alerts
    @GetMapping
    public List<Alerts> getUserAlerts(
            @RequestHeader("Authorization") String authHeader
    ) {
        Long userId = extractUserId(authHeader);
        return alertService.getUserAlerts(userId);
    }

    // ✅ Delete alert
    @DeleteMapping("/{id}")
    public void deleteAlert(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader
    ) {
        Long userId = extractUserId(authHeader);
        alertService.deleteAlert(id, userId);
    }

    // ✅ Manual trigger (for testing / cron alternative)
    @PostMapping("/check")
    public String triggerCheck() {
        alertService.checkAlerts();
        return "Alert check completed";
    }

    // 🔹 Helper
    private Long extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtService.extractUserId(token);
    }
}
