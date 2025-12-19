package com.cryptotracker.backend.transactions;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String asset;   // BTC, ETH, SOL

    private String type;    // BUY / SELL

    private double quantity;

    private double price;

    private LocalDateTime createdAt;
}
