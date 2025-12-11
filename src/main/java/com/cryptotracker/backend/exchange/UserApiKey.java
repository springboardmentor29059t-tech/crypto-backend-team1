package com.cryptotracker.backend.exchange;

import com.cryptotracker.backend.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_api_key")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @Column(name = "api_key_value")
    private String apiKeyValue;

    @Column(name = "api_secret_value")
    private String apiSecretValue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;
}
