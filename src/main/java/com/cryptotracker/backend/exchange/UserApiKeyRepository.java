package com.cryptotracker.backend.exchange;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserApiKeyRepository extends JpaRepository<UserApiKey, Long> {

    // 👉 Fetch all API keys stored by a specific user
    List<UserApiKey> findByUserId(Long userId);
}
