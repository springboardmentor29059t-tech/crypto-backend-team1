package com.crypto.portfolio_tracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.portfolio_tracker.dto.AddTradeRequest;
import com.crypto.portfolio_tracker.model.Trades;
import com.crypto.portfolio_tracker.repository.TradesRepository;

@RestController
@RequestMapping("/api/trades")
public class TradesController {

    private final TradesRepository tradesRepository;

    public TradesController(TradesRepository tradesRepository) {
        this.tradesRepository = tradesRepository;
    }

    // Add trade (buy/sell)
    @PostMapping("/add")
    public ResponseEntity<String> addTrade(@RequestBody AddTradeRequest request) {

        Trades trade = new Trades();
        trade.setUserId(request.getUserId());
        trade.setAssetSymbol(request.getAssetSymbol());
        trade.setSide(request.getSide());
        trade.setQuantity(request.getQuantity());
        trade.setPrice(request.getPrice());
        trade.setFee(request.getFee());
        trade.setExchangeId(request.getExchangeId());

        tradesRepository.save(trade);

        return ResponseEntity.ok("Trade added successfully");
    }

    // Get trade history for user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Trades>> getUserTrades(@PathVariable Long userId) {
        return ResponseEntity.ok(tradesRepository.findByUserId(userId));
    }
}
