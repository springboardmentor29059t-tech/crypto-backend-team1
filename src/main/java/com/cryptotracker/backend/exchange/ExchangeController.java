package com.cryptotracker.backend.exchange;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeRepository exchangeRepository;

    @GetMapping("/list")
    public List<Exchange> getAllExchanges() {
        return exchangeRepository.findAll();
    }
}
