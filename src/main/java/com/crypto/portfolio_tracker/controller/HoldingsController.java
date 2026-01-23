package com.crypto.portfolio_tracker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.portfolio_tracker.dto.AddHoldingRequest;
import com.crypto.portfolio_tracker.dto.UpdateHoldingRequest;
import com.crypto.portfolio_tracker.model.Holdings;
import com.crypto.portfolio_tracker.repository.HoldingsRepository;

@RestController
@RequestMapping("/api/holdings")
public class HoldingsController {

    private final HoldingsRepository holdingsRepository;

    public HoldingsController(HoldingsRepository holdingsRepository) {
        this.holdingsRepository = holdingsRepository;
    }

    // 1️⃣ Add holding manually
    @PostMapping("/add")
    public ResponseEntity<String> addHolding(@RequestBody AddHoldingRequest request) {

        Holdings holdings = new Holdings();
        holdings.setUserId(request.getUserId());
        holdings.setAssetSymbol(request.getAssetSymbol());
        holdings.setQuantity(request.getQuantity());
        holdings.setAvgCost(request.getAvgCost());
        holdings.setWalletType(request.getWalletType());
        holdings.setExchangeId(request.getExchangeId());

        holdingsRepository.save(holdings);

        return ResponseEntity.ok("Holding added successfully");
    }

    // 2️⃣ Get all holdings for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Holdings>> getUserHoldings(@PathVariable Long userId) {
        return ResponseEntity.ok(holdingsRepository.findByUserId(userId));
    }

    // 3️⃣ Update holding (quantity / avg cost)
    @PutMapping("/update/{holdingId}")
    public ResponseEntity<String> updateHolding(
            @PathVariable Long holdingId,
            @RequestBody UpdateHoldingRequest request) {

        Optional<Holdings> optionalHolding = holdingsRepository.findById(holdingId);

        if (optionalHolding.isEmpty()) {
            return ResponseEntity.badRequest().body("Holding not found");
        }

        Holdings holdings = optionalHolding.get();
        holdings.setQuantity(request.getQuantity());
        holdings.setAvgCost(request.getAvgCost());

        holdingsRepository.save(holdings);

        return ResponseEntity.ok("Holding updated successfully");
    }

    // 4️⃣ Delete holding
    @DeleteMapping("/delete/{holdingId}")
    public ResponseEntity<String> deleteHolding(@PathVariable Long holdingId) {

        if (!holdingsRepository.existsById(holdingId)) {
            return ResponseEntity.badRequest().body("Holding not found");
        }

        holdingsRepository.deleteById(holdingId);

        return ResponseEntity.ok("Holding deleted successfully");
    }
}
