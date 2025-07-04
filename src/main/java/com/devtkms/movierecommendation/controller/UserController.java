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
 * Controller that handles user authentication, registration, logout, and retrieval of own info.
 * Uses JWT-based stateless authentication.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Register a new user and log in at the same time by issuing a JWT in HttpOnly Cookie.
     *
     * @param userDto User registration request DTO
     * @param response HttpServletResponse (used to set cookies)
     * @return Registered user info (includes nickname)
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
     * Authenticates the user and issues a JWT in HttpOnly Cookie.
     *
     * @param loginRequestDto Login credentials (userId and password)
     * @param response HttpServletResponse (used to set cookies)
     * @return On success: user info, on failure: 401 Unauthorized
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

            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(true) // should be true in production (HTTPS only)
                    .path("/")
                    .maxAge(60 * 60 * 24 * 7) // 7 days
                    .sameSite("None")         // should be None in production (HTTPS only)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            LoginResponseDto dto = new LoginResponseDto(user.getId(), user.getNickname());
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Logs out the user by invalidating the JWT cookie.
     *
     * @param response HttpServletResponse (used to clear the cookie)
     * @return HTTP 200 OK (no body)
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .path("/")
                .httpOnly(true)
                .secure(true) // only in production
                .maxAge(0)    // expire immediately
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieve information of the currently authenticated user.
     *
     * @param authentication Spring Security-managed authentication info
     * @return User info (with nickname), or 401 if unauthenticated
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userService.findByUserId(userDetails.getUsername());
        return ResponseEntity.ok(new LoginResponseDto(user.getId(), user.getNickname()));
    }
}