package com.cryptotracker.backend.transactions;

import com.cryptotracker.backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionsService transactionsService;

    @GetMapping
    public List<Transaction> getTransactions(
            @AuthenticationPrincipal User user
    ) {
        return transactionsService.getTransactions(user.getId());
    }

    @PostMapping
    public Transaction addTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody Transaction tx
    ) {
        return transactionsService.addTransaction(tx, user.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        transactionsService.deleteTransaction(id, user.getId());
    }
}
