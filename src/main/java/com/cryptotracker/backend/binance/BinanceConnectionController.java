package com.cryptotracker.backend.binance;

import com.cryptotracker.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/binance")
@RequiredArgsConstructor
public class BinanceConnectionController {

    private final JwtService jwtService;
    private final BinanceConnectorService binanceConnectorService;

    @GetMapping("/test/{keyId}")
    public ResponseEntity<String> testBinance(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long keyId
    ) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractUserId(token);

        boolean success = binanceConnectorService.testConnection(userId, keyId);

        return ResponseEntity.ok(
                success ? "Connection successful" : "Connection failed"
        );
    }
}
