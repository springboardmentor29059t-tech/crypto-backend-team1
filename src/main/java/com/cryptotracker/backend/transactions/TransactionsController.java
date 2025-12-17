package com.cryptotracker.backend.transactions;

import com.cryptotracker.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionsService transactionsService;
    private final JwtService jwtService;

    @GetMapping
    public List<Transaction> getTransactions(
            @RequestHeader("Authorization") String authHeader
    ) {
        Long userId = extractUserId(authHeader);
        return transactionsService.getTransactions(userId);
    }

    @PostMapping
    public Transaction addTransaction(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Transaction tx
    ) {
        Long userId = extractUserId(authHeader);
        return transactionsService.addTransaction(tx, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id
    ) {
        Long userId = extractUserId(authHeader);
        transactionsService.deleteTransaction(id, userId);
    }

    private Long extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtService.extractUserId(token);
    }
}
