package com.cryptotracker.backend.exchange;

import com.cryptotracker.backend.security.EncryptionService;
import com.cryptotracker.backend.user.User;
import com.cryptotracker.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserApiKeyService {

    private final UserRepository userRepository;
    private final ExchangeRepository exchangeRepository;
    private final UserApiKeyRepository userApiKeyRepository;
    private final EncryptionService encryptionService;

    public UserApiKey saveUserApiKeys(Long userId, Long exchangeId, String apiKey, String apiSecret, String label) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        UserApiKey keys = UserApiKey.builder()
                .user(user)
                .exchange(exchange)
                .apiKeyValue(encryptionService.encrypt(apiKey))
                .apiSecretValue(encryptionService.encrypt(apiSecret))
                .label(label)
                .build();

        return userApiKeyRepository.save(keys);
    }

    public List<UserApiKey> getUserKeys(Long userId) {
        return userApiKeyRepository.findByUserId(userId);
    }

    public void deleteKey(Long keyId, Long userId) {
        UserApiKey key = userApiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("Key not found"));

        if (!key.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        userApiKeyRepository.delete(key);
    }

    public String getDecryptedKey(Long keyId) {
        UserApiKey key = userApiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("Key not found"));

        return encryptionService.decrypt(key.getApiKeyValue());
    }

    public String getDecryptedSecret(Long keyId) {
        UserApiKey key = userApiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("Key not found"));

        return encryptionService.decrypt(key.getApiSecretValue());
    }
}
