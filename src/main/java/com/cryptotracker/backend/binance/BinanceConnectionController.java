package com.cryptotracker.backend.binance;

import com.cryptotracker.backend.user.User;
import com.cryptotracker.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/binance")
@RequiredArgsConstructor
public class BinanceConnectionController {

    private final BinanceConnectorService binanceConnectorService;
    private final UserRepository userRepository;

    // ---------------------- TEST CONNECTION ---------------------- //

    @PostMapping("/test")
    public ResponseEntity<?> testConnection(
            @RequestBody Map<String, String> request,
            Principal principal
    ) {

        String apiKey = request.get("apiKey");
        String apiSecret = request.get("apiSecret");

        if (apiKey == null || apiSecret == null) {
            return ResponseEntity.badRequest().body("Missing API key or secret");
        }

        // Verify logged-in user
        User user = userRepository.findByEmail(principal.getName())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        // Make Binance test call
        boolean result =
                binanceConnectorService.testConnection(apiKey, apiSecret);

        return ResponseEntity.ok(result);
    }
}
