package com.cryptotracker.backend.exchange;

import com.cryptotracker.backend.exchange.dto.UserApiKeyRequest;
import com.cryptotracker.backend.user.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserApiKeyService {

    private final UserApiKeyRepository userApiKeyRepository;
    private final ExchangeRepository exchangeRepository;

    // ---------------------------
    // SAVE KEY
    // ---------------------------
    public UserApiKey saveApiKey(User user, UserApiKeyRequest request) {

        var exchange = exchangeRepository.findById(request.getExchangeId())
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        UserApiKey apiKey = UserApiKey.builder()
                .user(user)
                .exchange(exchange)
                .apiKey(request.getApiKey())
                .apiSecret(request.getApiSecret())
                .label(request.getLabel())
                .build();

        return userApiKeyRepository.save(apiKey);
    }

    // ---------------------------
    // GET ALL USER KEYS
    // ---------------------------
    public List<UserApiKey> getUserKeys(Long userId) {
        return userApiKeyRepository.findByUserId(userId);
    }

    // ---------------------------
    // DELETE KEY
    // ---------------------------
    public void deleteKey(Long keyId, Long userId) {
        var key = userApiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("API Key not found"));

        if (!key.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this key");
        }

        userApiKeyRepository.delete(key);
    }
}

