package com.cryptotracker.backend.exchange;

import com.cryptotracker.backend.common.EncryptionService;
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

    /** -----------------------------------------------
     *  SAVE USER API KEYS (Encrypt API key + secret)
     *  ----------------------------------------------- */
    public UserApiKey saveUserApiKeys(Long userId, Long exchangeId, String apiKey, String apiSecret, String label) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        UserApiKey keys = UserApiKey.builder()
                .user(user)
                .exchange(exchange)
                .apiKeyValue(encrypt(apiKey))
                .apiSecretValue(encrypt(apiSecret))
                .label(label)
                .build();

        return userApiKeyRepository.save(keys);
    }

    /** -----------------------------------------------
     *  LIST USER KEYS (masked)
     *  ----------------------------------------------- */
    public List<UserApiKey> getUserKeys(Long userId) {
        List<UserApiKey> keys = userApiKeyRepository.findByUserId(userId);

        // Return masked values instead of real ones
        keys.forEach(k -> {
            k.setApiKeyValue(mask(k.getApiKeyValue()));
            k.setApiSecretValue(mask(k.getApiSecretValue()));
        });

        return keys;
    }

    /** -----------------------------------------------
     *  DELETE USER KEY
     *  ----------------------------------------------- */
    public void deleteKey(Long keyId, Long userId) {
        UserApiKey key = userApiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("Key not found"));

        if (!key.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        userApiKeyRepository.delete(key);
    }

    /** -----------------------------------------------
     *  DECRYPT KEY (for Binance usage ONLY)
     *  ----------------------------------------------- */
    public String getDecryptedKey(Long keyId) {
        UserApiKey key = userApiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("Key not found"));

        return decrypt(key.getApiKeyValue());
    }

    public String getDecryptedSecret(Long keyId) {
        UserApiKey key = userApiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("Key not found"));

        return decrypt(key.getApiSecretValue());
    }

    /** -----------------------------------------------
     *  INTERNAL HELPERS
     *  ----------------------------------------------- */

    private String encrypt(String value) {
        try {
            return encryptionService.encrypt(value);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    private String decrypt(String value) {
        try {
            return encryptionService.decrypt(value);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }

    private String mask(String encryptedValue) {
        // Prevent decrypting all keys just for masking → too slow
        return "****-****-****";
    }
}
