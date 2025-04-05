package com.devtkms.movierecommendation.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private String nickname;

    public LoginResponseDto(String token, String nickname) {
        this.token = token;
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public String getNickname() {
        return nickname;
    }
}