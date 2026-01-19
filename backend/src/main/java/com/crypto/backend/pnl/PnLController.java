package com.crypto.backend.pnl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PnLController {

    @Autowired private PnLService pnLService;

    // Week 7: P&L Summary
    @GetMapping("/portfolio/{userId}/pnl")
    public PnLSummary getPnL(@PathVariable Long userId) {
        return pnLService.calculatePnL(userId);
    }

    // Week 7: CSV Export
    @GetMapping("/portfolio/{userId}/export")
    public ResponseEntity<byte[]> exportCsv(@PathVariable Long userId) {
        String csv = generateTradeCsv(userId);
        byte[] bytes = csv.getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "trades.csv");
        headers.setContentLength(bytes.length);

        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    private String generateTradeCsv(Long userId) {
        return "Date,Symbol,Side,Qty,Price,Fee,PnL\n" +
                "2026-01-01,BTC,BUY,0.1,90000,5.0,0\n" +
                "2026-01-02,BTC,SELL,0.1,95000,5.0,4500\n";
    }
}
