package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.LoginRequestDto;
import com.devtkms.movierecommendation.dto.UserRegisterRequestDto;
import com.devtkms.movierecommendation.entity.UserEntity;
import com.devtkms.movierecommendation.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    public UserController(UserService userService) {
        this.userService = userService;
    }

        @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDto userDto) {
        try {
            UserEntity user = userService.registerUser(userDto);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        // ★ メールアドレスとパスワードを使って未認証のトークンを作成
        UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        loginRequestDto.getEmail(), loginRequestDto.getPassword());

        // ★ 認証マネージャーを使ってユーザーを認証
        Authentication authentication = authenticationManager.authenticate(token);

        // ★ 空のセキュリティコンテキストを作成し、認証情報を設定
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);

        // ★ セッションにセキュリティコンテキストを保存
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        // ★ データベースからログインユーザー情報を取得
        UserEntity user = userService.findByEmail(loginRequestDto.getEmail());

        // ★ ログインユーザー情報をレスポンスとして返す
        return ResponseEntity.ok(user);
    }
}