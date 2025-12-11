package com.cryptotracker.backend.exchange.controller;

import com.cryptotracker.backend.apikeys.ApiKeys;
import com.cryptotracker.backend.apikeys.ApiKeysService;
import com.cryptotracker.backend.exchange.dto.ApiKeyRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ApiKeysService apiKeysService;

    @PostMapping("/keys")
    public ApiKeys saveKey(@RequestBody ApiKeyRequestDTO dto){
        return apiKeysService.saveKey(
                dto.getUserId(),
                dto.getExchangeId(),
                dto.getApiKey(),
                dto.getApiSecret(),
                dto.getLabel()
        );
    }

    @GetMapping("/keys/user/{userId}")
    public List<ApiKeys> getUserKeys(@PathVariable Long userId) {
        return apiKeysService.getKeys(userId);
    }

    @DeleteMapping("/keys/{id}/user/{userId}")
    public void deleteKey(@PathVariable Long id, @PathVariable Long userId){
        apiKeysService.deleteKey(id, userId);
    }
}
