package com.cryptotracker.backend.transactions;

import com.cryptotracker.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionRepository transactionRepository;
    private final JwtService jwtService;

    // 🔹 GET all transactions for logged-in user
    @GetMapping
    public List<Transaction> getTransactions(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return transactionRepository.findByUserId(userId);
    }

    // 🔹 ADD a new transaction
    @PostMapping
    public Transaction addTransaction(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Transaction transaction
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        transaction.setUserId(userId);
        return transactionRepository.save(transaction);
    }

    // 🔹 DELETE a transaction
    @DeleteMapping("/{id}")
    public void deleteTransaction(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // safety check
        if (!tx.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        transactionRepository.deleteById(id);
    }
}
