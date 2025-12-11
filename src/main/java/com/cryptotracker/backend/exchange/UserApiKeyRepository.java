package com.cryptotracker.backend.exchange;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserApiKeyRepository extends JpaRepository<UserApiKey, Long> {
    List<UserApiKey> findByUserId(Long userId);
}
