package com.payflow.api_gateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public void validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}