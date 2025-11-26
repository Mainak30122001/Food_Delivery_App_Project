package com.example.food_delivery_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    // 256-bit secret for HS256
    private static final String SECRET =
            "1234567890123456789012345678901234567890123456789012345678901234";

    // Generate JWT token
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 60 * 1000)) // 10 hours
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get signing key
    public Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Extract claims safely
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    // Extract username
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // Check if token expired
    private boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    // Validate token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
