package com.crypto.backend.risk;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scam_tokens")
public class ScamToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_address", nullable = false)
    private String contractAddress;

    @Column(nullable = false)
    private String chain; // e.g., "ETH", "BSC"

    @Column(name = "risk_level")
    private String riskLevel; // 'low', 'medium', 'high'

    private String source; // e.g., "CryptoScamDB"

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    // Constructors
    public ScamToken() {}

    public ScamToken(String contractAddress, String chain, String riskLevel, String source) {
        this.contractAddress = contractAddress;
        this.chain = chain;
        this.riskLevel = riskLevel;
        this.source = source;
        this.lastSeen = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getContractAddress() { return contractAddress; }
    public String getChain() { return chain; }
    public String getRiskLevel() { return riskLevel; }
    public String getSource() { return source; }
    public LocalDateTime getLastSeen() { return lastSeen; }
}
