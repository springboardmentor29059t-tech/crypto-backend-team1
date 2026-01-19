package com.crypto.backend.pnl;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TaxController {

    @GetMapping("/tax/hints/{userId}")
    public Map<String, Object> getTaxHints(@PathVariable Long userId) {
        return Map.of(
                "short_term", "Trades < 365 days: 30% tax (India)",
                "long_term", "Trades > 365 days: 20% tax (India)",
                "total_gains", "$6,500",
                "recommendation", "Consult tax professional"
        );
    }
}
