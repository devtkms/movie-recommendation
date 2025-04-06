package com.devtkms.movierecommendation.dto;

import lombok.Data;

@Data
public class UserRegisterResponseDto {
    private Long id;
    private String token;
    private String nickname;

    public UserRegisterResponseDto(Long id, String token, String nickname) {
        this.id = id;
        this.token = token;
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getNickname() {
        return nickname;
    }
}
