package com.devtkms.movierecommendation.security;

import com.devtkms.movierecommendation.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final String userId;  // ユーザーのユーザーID
    private final String password; // ユーザーのパスワード

    public CustomUserDetails(UserEntity user) {
        if (user == null || user.getUserId() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("UserEntity or its fields cannot be null");
        }
        this.userId = user.getUserId();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 権限なし
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId; // Spring Security のユーザー名は userId を返す
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}