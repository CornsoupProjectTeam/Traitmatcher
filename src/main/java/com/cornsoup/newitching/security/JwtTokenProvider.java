package com.cornsoup.newitching.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyPlain;

    private Key key;

    private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1시간

    @PostConstruct
    public void init() {
        if (secretKeyPlain.length() < 32) {
            throw new IllegalArgumentException("SECRET_KEY는 최소 32자 이상이어야 합니다.");
        }
        this.key = Keys.hmacShaKeyFor(secretKeyPlain.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String memberId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
