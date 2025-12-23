package com.crypto.backend.trade;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    // List all trades for a user
    @GetMapping("/{userId}")
    public List<Trade> getTrades(@PathVariable Long userId) {
        return tradeService.getTrades(userId);
    }

    // Manually add a trade for a user
    @PostMapping("/{userId}")
    public Trade addTrade(@PathVariable Long userId, @RequestBody TradeRequest request) {

        Long exchangeId = request.getExchangeId();
        String assetSymbol = request.getAssetSymbol();
        String side = request.getSide();
        BigDecimal quantity = request.getQuantity();
        BigDecimal price = request.getPrice();
        BigDecimal fee = request.getFee();
        LocalDateTime executedAt = request.getExecutedAt();

        return tradeService.addManualTrade(
                userId,
                exchangeId,
                assetSymbol,
                side,
                quantity,
                price,
                fee,
                executedAt
        );
    }
}
