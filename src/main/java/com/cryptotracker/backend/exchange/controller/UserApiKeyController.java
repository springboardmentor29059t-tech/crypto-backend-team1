package com.cryptotracker.backend.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.cryptotracker.backend.exchange.UserApiKey;
import com.cryptotracker.backend.exchange.UserApiKeyService;

import java.util.List;

@RestController
@RequestMapping("/api/keys")
@RequiredArgsConstructor
public class UserApiKeyController {

    private final UserApiKeyService userApiKeyService;

    @PostMapping("/add")
    public UserApiKey addKey(@RequestBody com.cryptotracker.backend.exchange.dto.UserApiKeyRequest request) {
        return userApiKeyService.saveUserApiKeys(
                request.getUserId(),
                request.getExchangeId(),
                request.getApiKey(),
                request.getApiSecret(),
                request.getLabel()
        );
    }

    @GetMapping("/{userId}")
    public List<UserApiKey> getKeys(@PathVariable Long userId) {
        return userApiKeyService.getUserKeys(userId);
    }

    @DeleteMapping("/{userId}/{keyId}")
    public void deleteKey(@PathVariable Long userId, @PathVariable Long keyId) {
        userApiKeyService.deleteKey(keyId, userId);
    }
}
