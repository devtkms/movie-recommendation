package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.config.JwtConfig;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service responsible for JWT generation and validation.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    /**
     * Token response structure
     */
    public record JwtToken(String token, String refreshToken, Date expiresAt) {}

    /**
     * Extract username from JWT token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract specific claim from JWT token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generate JWT token with default claims
     */
    public JwtToken generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generate JWT token with additional claims
     */
    public JwtToken generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        String accessToken = buildToken(extraClaims, userDetails, jwtConfig.getExpiration());
        String refreshToken = buildToken(new HashMap<>(), userDetails, jwtConfig.getRefreshExpiration());
        Date expiresAt = new Date(System.currentTimeMillis() + jwtConfig.getExpiration());

        return new JwtToken(accessToken, refreshToken, expiresAt);
    }

    /**
     * Build JWT token
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Validate token against user details
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Check if token is expired
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract expiration date from token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract all claims from token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Get signing key from configured secret
     */
    private javax.crypto.SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}