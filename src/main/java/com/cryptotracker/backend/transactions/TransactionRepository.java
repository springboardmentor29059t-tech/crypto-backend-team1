package com.cryptotracker.backend.transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    // ✅ CORRECT because Transaction has `userId` field
    List<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId);

}
