package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.LoginRequestDto;
import com.devtkms.movierecommendation.dto.LoginResponseDto;
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
            UserEntity user = userService.registerUser(userDto);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails).token();

            return ResponseEntity.ok(new LoginResponseDto(token, user.getNickname()));

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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserEntity user = userService.findByEmail(loginRequestDto.getEmail());

            // ✅ JwtToken から中身を取り出す
            String token = jwtService.generateToken(userDetails).token();

            return ResponseEntity.ok(new LoginResponseDto(token, user.getNickname()));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("認証に失敗しました: " + e.getMessage());
        }
    }
}