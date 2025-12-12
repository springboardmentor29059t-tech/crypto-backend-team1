package com.cryptotracker.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "b8e42f79c5a14ed2a9b7e0bafbf95012b8e42f79c5a14ed2a9b7e0bafbf95012"; // 64-char key

    private static final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ---------------------------
    // CREATE JWT WITH USER ID
    // ---------------------------
   public String generateToken(String subject) {
    return Jwts.builder()
            .setSubject(subject) // NOW userId, not email
            .setIssuedAt(new Date())
            .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
}


    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)        // 👈 SUB = USER ID
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ---------------------------
    // EXTRACT USER ID FROM TOKEN
    // ---------------------------
    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject(); // returns userId as String
    }

    public Long extractUserId(String token) {
        try {
            return Long.valueOf(extractSubject(token));
        } catch (Exception e) {
            return null;
        }
    }

    // ---------------------------
    // VALIDATE TOKEN
    // ---------------------------
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    // ---------------------------
    // INTERNAL CLAIM EXTRACTION
    // ---------------------------
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
