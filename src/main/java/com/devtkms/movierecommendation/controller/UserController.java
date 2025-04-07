package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.LoginRequestDto;
import com.devtkms.movierecommendation.dto.LoginResponseDto;
import com.devtkms.movierecommendation.dto.UserRegisterRequestDto;
import com.devtkms.movierecommendation.dto.UserRegisterResponseDto;
import com.devtkms.movierecommendation.entity.UserEntity;
import com.devtkms.movierecommendation.service.JwtService;
import com.devtkms.movierecommendation.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDto userDto, HttpServletResponse response) {
        try {
            UserEntity user = userService.registerUser(userDto);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails).token();

            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(60 * 60 * 24 * 7)
                    .sameSite("None")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok(new UserRegisterResponseDto(user.getId(), null, user.getNickname()));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * ログイン（JWT発行）
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserEntity user = userService.findByEmail(loginRequestDto.getEmail());

            String token = jwtService.generateToken(userDetails).token();

            // ✅ HttpOnly Cookie にトークンをセット
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(true) // 本番環境は true（HTTPSのみ）
                    .path("/")
                    .maxAge(60 * 60 * 24 * 7) // 7日間
                    .sameSite("None")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            // トークンは返さず、IDとニックネームのみ返す（セキュリティ強化）
            return ResponseEntity.ok(new LoginResponseDto(user.getId(), null, user.getNickname()));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("認証に失敗しました: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .path("/")
                .httpOnly(true)
                .secure(true) // 本番環境のみ
                .maxAge(0)    // 有効期限切れにする
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("未認証");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(new LoginResponseDto(user.getId(), null, user.getNickname()));
    }
}