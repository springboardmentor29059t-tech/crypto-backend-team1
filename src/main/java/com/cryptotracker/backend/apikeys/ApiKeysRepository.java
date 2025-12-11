package com.cryptotracker.backend.apikeys;

import com.cryptotracker.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApiKeysRepository extends JpaRepository<ApiKeys, Long> {
    List<ApiKeys> findByUser(User user);
}
