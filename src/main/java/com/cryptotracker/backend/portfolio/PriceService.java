package com.cryptotracker.backend.portfolio;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PriceService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchPrices(String ids) {
        String url =
                "https://api.coingecko.com/api/v3/simple/price" +
                "?ids=" + ids +
                "&vs_currencies=usd";

        return restTemplate.getForObject(url, String.class);
    }
}
