package com.cryptotracker.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "YOURSUPERSECRETKEYYOURSUPERSECRETKEY12"; // 32+ chars for HS256
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // -----------------------------
    // CREATE TOKEN USING userId
    // -----------------------------
    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // convert Long to String
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // -----------------------------
    // EXTRACT USER ID FROM TOKEN
    // -----------------------------
    public Long extractUserId(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.parseLong(subject);
    }

    // -----------------------------
    // VALIDATE TOKEN
    // -----------------------------
    public boolean isTokenValid(String token) {
        try {
            extractUserId(token); // this will throw if invalid
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
