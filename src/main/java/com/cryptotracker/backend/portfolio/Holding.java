package com.cryptotracker.backend.portfolio;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "holdings")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String asset;

    private double quantity;

    private double avgBuyPrice;
}
