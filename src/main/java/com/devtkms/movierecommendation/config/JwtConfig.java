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
     * シークレットキー（Base64エンコード）
     */
    private String secret;

    /**
     * アクセストークンの有効期限（ミリ秒）
     */
    private long expiration;

    /**
     * リフレッシュトークンの有効期限（ミリ秒）
     */
    private long refreshExpiration;
}