package com.devtkms.movierecommendation.dto;

import lombok.Data;

@Data
public class UserRegisterRequestDto {
    private String email;
    private String password;
    private String nickname;

    private String useProviderName;
    private Integer useProviderId;

    private String favoriteMovieName;
    private Long favoriteMovieId;

    private String gender;
    private String ageGroup;
}