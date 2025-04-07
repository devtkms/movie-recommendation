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

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    /**
     * トークンレスポンス
     */
    public record JwtToken(String token, String refreshToken, Date expiresAt) {
    }

    /**
     * トークンの抽出
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * クレームの抽出
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * トークンの生成
     */
    public JwtToken generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * 追加のクレームを含むトークンを生成
     */
    public JwtToken generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {
        // アクセストークンの生成
        String accessToken = buildToken(extraClaims, userDetails, jwtConfig.getExpiration());

        // リフレッシュトークンの生成（通常は最小限のクレームを含む）
        String refreshToken = buildToken(new HashMap<>(), userDetails, jwtConfig.getRefreshExpiration());

        // トークンの有効期限
        Date expiresAt = new Date(System.currentTimeMillis() + jwtConfig.getExpiration());

        return new JwtToken(accessToken, refreshToken, expiresAt);
    }

    /**
     * トークンのビルド
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * トークンの有効性を確認
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * トークンの期限切れを確認
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * トークンの期限切れを抽出
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * SignInKeyの取得
     */
    private javax.crypto.SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
