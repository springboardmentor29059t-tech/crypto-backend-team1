package com.cryptotracker.backend.exchange;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserApiKeyRepository extends JpaRepository<UserApiKey, Long> {

    // ✅ Correct way to access foreign key ID
    List<UserApiKey> findByUser_Id(Long userId);
}
