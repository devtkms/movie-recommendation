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

/**
 * ユーザーの認証・登録・ログアウト・自身の情報取得を行うコントローラー。
 * JWTベースの認証を用いてセッションレスで管理される。
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * ユーザー登録と同時にログインし、JWTをHttpOnly Cookieに発行する。
     *
     * @param userDto ユーザー登録用リクエストDTO
     * @param response レスポンス（クッキー追加のために使用）
     * @return 登録されたユーザー情報（ニックネーム付き）
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDto userDto, HttpServletResponse response) {
        try {
            UserEntity user = userService.registerUser(userDto);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUserId(), userDto.getPassword())
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


            UserRegisterResponseDto dto = new UserRegisterResponseDto(user.getId(), user.getNickname());
            return ResponseEntity.ok(dto);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * ユーザー認証を行い、JWTをHttpOnly Cookieに発行する。
     *
     * @param loginRequestDto ログイン情報（ユーザーIDとパスワード）
     * @param response レスポンス（クッキー追加のために使用）
     * @return 認証成功時：ユーザー情報、失敗時：401エラー
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUserId(),
                            loginRequestDto.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserEntity user = userService.findByUserId(loginRequestDto.getUserId());

            String token = jwtService.generateToken(userDetails).token();

            // HttpOnly Cookie にトークンをセット
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(true) // 本番環境は true（HTTPSのみ）
                    .path("/")
                    .maxAge(60 * 60 * 24 * 7) // 7日間
                    .sameSite("None") // 本番環境は None（HTTPSのみ）
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            LoginResponseDto dto = new LoginResponseDto(user.getId(), user.getNickname());
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("認証に失敗しました: " + e.getMessage());
        }
    }

    /**
     * JWT Cookie を無効化し、ログアウト処理を行う。
     *
     * @param response レスポンス（Cookieの削除用）
     * @return HTTP 200 OK（空のレスポンス）
     */
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

    /**
     * 認証中のユーザー情報を取得する。
     *
     * @param authentication Spring Security が管理する認証情報
     * @return ログインユーザーの情報（ニックネーム付き）、未認証時は401
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("未認証");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userService.findByUserId(userDetails.getUsername());
        return ResponseEntity.ok(new LoginResponseDto(user.getId(), user.getNickname()));
    }
}