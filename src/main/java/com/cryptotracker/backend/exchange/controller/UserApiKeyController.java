package com.cryptotracker.backend.exchange.controller;

import com.cryptotracker.backend.exchange.UserApiKeyService;
import com.cryptotracker.backend.exchange.UserApiKey;
import com.cryptotracker.backend.exchange.dto.UserApiKeyRequest;
import com.cryptotracker.backend.user.User;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class UserApiKeyController {

    private final UserApiKeyService userApiKeyService;

    // ----------------------------------------------------
    // 1️⃣ SAVE API KEY
    // ----------------------------------------------------
    @PostMapping("/keys")
    public UserApiKey saveKey(
            @AuthenticationPrincipal User user,
            @RequestBody UserApiKeyRequest request) {

        return userApiKeyService.saveApiKey(user, request);
    }

    // ----------------------------------------------------
    // 2️⃣ GET USER KEYS
    // ----------------------------------------------------
    @GetMapping("/keys")
    public List<UserApiKey> getKeys(@AuthenticationPrincipal User user) {
        return userApiKeyService.getUserKeys(user.getId());
    }

    // ----------------------------------------------------
    // 3️⃣ DELETE SPECIFIC KEY
    // ----------------------------------------------------
    @DeleteMapping("/keys/{keyId}")
    public String deleteKey(
            @AuthenticationPrincipal User user,
            @PathVariable Long keyId) {

        userApiKeyService.deleteKey(keyId, user.getId());
        return "API Key deleted successfully";
    }
}
