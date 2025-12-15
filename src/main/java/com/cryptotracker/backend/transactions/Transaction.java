package com.cryptotracker.backend.transactions;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String asset;   // BTC
    private String type;    // BUY / SELL
    private double quantity;
    private double price;

    private LocalDate date;
}
