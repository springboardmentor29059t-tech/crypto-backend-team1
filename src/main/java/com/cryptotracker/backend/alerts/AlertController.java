package com.cryptotracker.backend.alerts;

import com.cryptotracker.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService service;
    private final JwtService jwtService;

    // ✅ Create alert
    @PostMapping
    public Alerts create(
            @RequestHeader("Authorization") String auth,
            @RequestBody AlertRequest req
    ) {
        Long userId = extractUserId(auth);
        return service.createAlert(
                userId,
                req.coinId(),
                req.price(),
                req.type()
        );
    }

    // ✅ Get user alerts
    @GetMapping
    public List<Alerts> getAlerts(
            @RequestHeader("Authorization") String auth
    ) {
        return service.getUserAlerts(extractUserId(auth));
    }

    // ✅ Delete alert
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth
    ) {
        service.deleteAlert(id, extractUserId(auth));
    }

    // ✅ Manual trigger (optional)
    @PostMapping("/check")
    public String checkNow() {
        service.checkAlerts();
        return "Alerts checked";
    }

    private Long extractUserId(String auth) {
        String token = auth.replace("Bearer ", "");
        return jwtService.extractUserId(token);
    }
}
