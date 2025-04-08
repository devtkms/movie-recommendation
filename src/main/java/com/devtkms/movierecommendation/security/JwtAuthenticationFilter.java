package com.devtkms.movierecommendation.security;

import java.io.IOException;

import com.devtkms.movierecommendation.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * JWT認証フィルター
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * ロガー
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    /**
     * JWTサービス
     */
    private final JwtService jwtService;

    /**
     * ユーザー詳細サービス
     */
    private final UserDetailsService userDetailsService;

    /**
     * フィルター内部
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String jwt = extractToken(request); // 👈 修正ポイント：トークンの取得を関数化
        final String userId;

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            userId = jwtService.extractUsername(jwt);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            logger.error("JWT token validation failed: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        // Authorizationヘッダーから取得
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " を除く
        }

        // Cookie から取得
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null; // トークンが見つからなければ null
    }
}
