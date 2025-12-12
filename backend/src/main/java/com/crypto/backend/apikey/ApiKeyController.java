package com.crypto.backend.apikey;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apikeys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    // POST /api/apikeys
    @PostMapping
    public ApiKey create(@RequestBody ApiKeyCreateRequest request) {
        return apiKeyService.createApiKey(request);
    }

    // GET /api/apikeys/user/{userId}
    @GetMapping("/user/{userId}")
    public List<ApiKey> getUserKeys(@PathVariable Long userId) {
        return apiKeyService.getKeysByUserId(userId);
    }
}
