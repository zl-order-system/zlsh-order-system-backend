package net.octoberserver.ordersystem.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${JWT_KEY}")
    private final String SIGNING_KEY = "e943eac6985459fc302d77532fd558a347f20c8a70e11ccad053a7a697ebb2d5";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SIGNING_KEY.getBytes());
    }

    public String generateToken(UserDetails user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder()
            .claims(extraClaims)
            .subject(user.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .signWith(getSigningKey())
            .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractClaims(token));
    }

    public Optional<String> extractUserID(String token) {
        String subject;
        try {
            subject = extractClaim(token, Claims::getSubject);
            Long.parseLong(subject);
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(subject);
    }

    private Claims extractClaims(String token) {
        return Jwts
            .parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
