package com.wuubangdev.portfolio.infrastructure.global.security.jwt;

import com.wuubangdev.portfolio.infrastructure.global.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties; // Inject class config vào

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 1. Tạo Token từ thông tin Username
    public String generateToken(String username) {
        return generateAccessToken(username);
    }

    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpirationMs());

        return Jwts.builder()
                .subject(username)
                .id(UUID.randomUUID().toString())
                .claims(Map.of("type", "access"))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshExpirationMs());

        return Jwts.builder()
                .subject(username)
                .id(UUID.randomUUID().toString())
                .claims(Map.of("type", "refresh"))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // 2. Lấy Username từ trong Token
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // 3. Kiểm tra Token có hợp lệ không
    public boolean validateToken(String authToken) {
        return parseClaims(authToken) != null;
    }

    public boolean validateRefreshToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        return claims != null && "refresh".equals(claims.get("type", String.class));
    }

    public long getAccessTokenExpirationMs() {
        return jwtProperties.getExpirationMs();
    }

    public long getRefreshTokenExpirationMs() {
        return jwtProperties.getRefreshExpirationMs();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return null;
    }
}
