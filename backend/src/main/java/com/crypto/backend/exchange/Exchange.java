package com.crypto.backend.exchange;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "exchanges")
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;   // e.g., BINANCE, COINBASE

    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
