package com.taekcheonkim.todolist.user.util;

import com.taekcheonkim.todolist.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@ConditionalOnProperty(name = "user.authentication.method", havingValue = "jwt")
public class JwtGenerator {
    public final Long JWT_EXPIRATION;
    public final String JWT_SECRET;

    public JwtGenerator(
            @Value("${user.authentication.options.expiration}") Long jwtExpiration,
            @Value("${user.authentication.options.secret}") String jwtSecret
    ) {
        this.JWT_EXPIRATION = jwtExpiration;
        this.JWT_SECRET = jwtSecret;
    }

    public boolean validateToken(String token) {
        SecretKey key = getSecretKey();
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Jwt was expired or incorrect.");
        }
    }

    public String getEmailFromToken(String token) {
        SecretKey key = getSecretKey();
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public String generateToken(User user) {
        String username = user.getEmail();
        Date currentDate = new Date();
        long expireTime = currentDate.getTime() + JWT_EXPIRATION;
        Date expireDate = new Date(expireTime);

        SecretKey key = getSecretKey();
        return Jwts.builder()
                .subject(username)
                .issuedAt(expireDate)
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }
}