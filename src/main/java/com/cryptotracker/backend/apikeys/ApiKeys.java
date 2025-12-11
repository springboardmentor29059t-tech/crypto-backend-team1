package com.cryptotracker.backend.apikeys;

import com.cryptotracker.backend.exchange.Exchange;
import com.cryptotracker.backend.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "api_keys")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiKeys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // USER → many api keys
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // EXCHANGE → many api keys
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id", nullable = false)
    private Exchange exchange;

    @Column(name = "api_key", nullable = false, length = 1024)
    private String apiKey; // encrypted text

    @Column(name = "api_secret", nullable = false, length = 1024)
    private String apiSecret; // encrypted text

    @Column(length = 255)
    private String label; // user-friendly name

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public ApiKeys saveKey(Long userId, Long exchangeId, String apiKey2, String apiSecret2, String label2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveKey'");
    }

    public List<ApiKeys> getKeys(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getKeys'");
    }

    public void deleteKey(Long id2, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteKey'");
    }
}
