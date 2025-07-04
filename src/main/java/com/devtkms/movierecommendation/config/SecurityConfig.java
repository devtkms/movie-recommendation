package com.devtkms.movierecommendation.config;

import com.devtkms.movierecommendation.security.CustomUserDetailsService;
import com.devtkms.movierecommendation.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Spring Security configuration class.
 * Configures JWT authentication, CORS, authorization, password encoding, session management, etc.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Defines security rules for the application.
     *
     * @param http HttpSecurity instance
     * @param userDetailsService Service to load user-specific data
     * @param passwordEncoder Password encoder
     * @return Configured security filter chain
     * @throws Exception on configuration errors
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomUserDetailsService userDetailsService,
                                                   PasswordEncoder passwordEncoder) throws Exception {
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
                        .requestMatchers("/api/users/login").permitAll()
                        .requestMatchers("/api/users/logout").permitAll()
                        .requestMatchers("/api/movie/overview").permitAll()
                        .requestMatchers("/api/movies/save").permitAll()
                        .requestMatchers("/api/movies/delete/{movieId}").permitAll()
                        .requestMatchers("/api/movies/${movieId}/detail").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf((csrf) -> csrf.disable())
                .cors((cors) -> cors
                        .configurationSource(apiConfigurationSource())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authenticationProvider(authenticationProvider(userDetailsService, passwordEncoder))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Defines the authentication manager.
     *
     * @param userDetailsService Service to load user-specific data
     * @param passwordEncoder Password encoder
     * @return Authentication manager
     */
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

    /**
     * Configuration for DaoAuthenticationProvider.
     *
     * @param userDetailsService Service to load user-specific data
     * @param passwordEncoder Password encoder
     * @return Authentication provider
     */
    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService,
                                                         PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Uses BCrypt as the password encoder.
     *
     * @return BCrypt password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Return a new BCryptPasswordEncoder
    }

    /**
     * Defines CORS configuration for API.
     *
     * @return CORS configuration source
     */
    @Bean
    CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",
                "http://localhost:3000",
                "https://movireco.onrender.com",
                "https://movireco.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cookie"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}





