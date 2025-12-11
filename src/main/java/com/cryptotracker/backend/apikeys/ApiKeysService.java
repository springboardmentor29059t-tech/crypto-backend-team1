package com.cryptotracker.backend.apikeys;

import com.cryptotracker.backend.exchange.Exchange;
import com.cryptotracker.backend.exchange.ExchangeRepository;
import com.cryptotracker.backend.user.User;
import com.cryptotracker.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiKeysService {

    private final ApiKeysRepository apiKeysRepository;
    private final UserRepository userRepository;
    private final ExchangeRepository exchangeRepository;

    public ApiKeys saveKey(Long userId, Long exchangeId, String apiKey, String apiSecret, String label) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        ApiKeys key = ApiKeys.builder()
                .user(user)
                .exchange(exchange)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .label(label)
                .build();

        return apiKeysRepository.save(key);
    }

    public List<ApiKeys> getKeys(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return apiKeysRepository.findByUser(user);
    }

    public void deleteKey(Long keyId, Long userId) {

        ApiKeys key = apiKeysRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("Key not found"));

        if (!key.getUser().getId().equals(userId)) {
            throw new RuntimeException("Key does not belong to this user");
        }

        apiKeysRepository.delete(key);
    }
}
