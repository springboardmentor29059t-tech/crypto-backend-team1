package com.cryptotracker.backend.exchange;

import com.cryptotracker.backend.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_api_key")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "exchange_id", nullable = false)
    private Exchange exchange;

    @Column(name = "api_key", nullable = false)
    private String apiKey;

    @Column(name = "api_secret", nullable = false)
    private String apiSecret;

    private String label;
}

