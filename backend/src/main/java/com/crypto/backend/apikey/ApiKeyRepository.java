package com.crypto.backend.apikey;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    // This allows us to find all API keys belonging to a specific user
    // Useful for showing a user their connected exchanges on the frontend later
    List<ApiKey> findByUserId(Long userId);
}
