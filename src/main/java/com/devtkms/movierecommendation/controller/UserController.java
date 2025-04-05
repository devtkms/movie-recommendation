package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.LoginRequestDto;
import com.devtkms.movierecommendation.dto.UserRegisterRequestDto;
import com.devtkms.movierecommendation.entity.UserEntity;
import com.devtkms.movierecommendation.service.JwtService;
import com.devtkms.movierecommendation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * ユーザー登録 + 自動ログイン（JWT発行）
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDto userDto) {
        try {
            // 1. 新規登録
            UserEntity user = userService.registerUser(userDto);

            // 2. 登録直後にログイン認証
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

            // 3. JWT生成して返す
            return ResponseEntity.ok(jwtService.generateToken((UserDetails) authentication.getPrincipal()));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * ログイン（JWT発行）
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            // 1. 認証
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword())
            );

            // 2. JWT生成して返す
            return ResponseEntity.ok(jwtService.generateToken((UserDetails) authentication.getPrincipal()));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("認証に失敗しました: " + e.getMessage());
        }
    }
}