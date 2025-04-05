package com.devtkms.movierecommendation.config;

import com.devtkms.movierecommendation.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/recommendations").permitAll()
                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/api/contact/submit").permitAll()
                        .requestMatchers("/api/contact/validate").permitAll()
                        .requestMatchers("/movie/{movieId}/watch/providers").permitAll()
                        .requestMatchers("/api/recommendations/personalize").permitAll()
                        .requestMatchers("/api/search/movies").permitAll()
                        .requestMatchers("/api/users/register").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf((csrf) -> csrf.disable())
                .cors((cors) -> cors
                        .configurationSource(apiConfigurationSource())
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService); // Set the custom UserDetailsService
        authenticationProvider.setPasswordEncoder(passwordEncoder); // Set the password encoder

        ProviderManager providerManager = new ProviderManager(authenticationProvider); // Create the authentication provider manager
        providerManager.setEraseCredentialsAfterAuthentication(false); // Prevent erasure of credentials after authentication

        return providerManager; // Return the configured AuthenticationManager
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Return a new BCryptPasswordEncoder
    }

    @Bean
    CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:3000"
                ,"https://movireco.onrender.com","https://movireco.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 必要なメソッドを許可
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // 必要なヘッダーを許可
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}





