package com.devtkms.movierecommendation.security;

import com.devtkms.movierecommendation.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of Spring Security's UserDetails interface.
 * Wraps the application's UserEntity for authentication purposes.
 */
public class CustomUserDetails implements UserDetails {

    private final String userId;   // User ID (used for login)
    private final String password; // User password (should be hashed)

    public CustomUserDetails(UserEntity user) {
        if (user == null || user.getUserId() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("UserEntity or its fields cannot be null");
        }
        this.userId = user.getUserId();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // No roles or authorities assigned
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account is always active
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return true; // User is always enabled
    }
}