package com.devtkms.movierecommendation.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtConfig {

    /**
     * Secret key (Base64 encoded)
     */
    private String secret;

    /**
     * Access token expiration time (in milliseconds)
     */
    private long expiration;

    /**
     * Refresh token expiration time (in milliseconds)
     */
    private long refreshExpiration;
}