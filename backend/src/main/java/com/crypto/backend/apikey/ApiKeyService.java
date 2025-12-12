package com.crypto.backend.apikey;

import com.crypto.backend.exchange.Exchange;
import com.crypto.backend.exchange.ExchangeRepository;
import com.crypto.backend.user.User;
import com.crypto.backend.user.UserRepository;
import com.crypto.backend.util.EncryptionUtil; // Import the new util
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;
    private final ExchangeRepository exchangeRepository;
    private final EncryptionUtil encryptionUtil; // Inject the util

    public ApiKeyService(ApiKeyRepository apiKeyRepository,
                         UserRepository userRepository,
                         ExchangeRepository exchangeRepository,
                         EncryptionUtil encryptionUtil) {
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
        this.exchangeRepository = exchangeRepository;
        this.encryptionUtil = encryptionUtil;
    }

    public ApiKey createApiKey(ApiKeyCreateRequest request) {
        // 1. Fetch the User
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Fetch the Exchange
        Exchange exchange = exchangeRepository.findById(request.getExchangeId())
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        // 3. Create and populate the ApiKey entity
        ApiKey apiKey = new ApiKey();
        apiKey.setUser(user);
        apiKey.setExchange(exchange);
        apiKey.setApiKey(request.getApiKey()); // Public key can be plain text

        // SECURITY UPDATE: Encrypt the secret before saving!
        String encryptedSecret = encryptionUtil.encrypt(request.getApiSecret());
        apiKey.setApiSecret(encryptedSecret);

        apiKey.setLabel(request.getLabel());
        apiKey.setCreatedAt(LocalDateTime.now());

        // 4. Save to DB
        return apiKeyRepository.save(apiKey);
    }

    public List<ApiKey> getKeysByUserId(Long userId) {
        return apiKeyRepository.findByUserId(userId);
    }

    // New helper method for Week 2 (Connecting to Binance)
    public String getDecryptedSecret(ApiKey apiKey) {
        return encryptionUtil.decrypt(apiKey.getApiSecret());
    }
}
