package com.devtkms.movierecommendation.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserEntity {
    private Long id;
    private String userId;
    private String password;
    private String nickname;
    private String useProviderName;
    private Integer useProviderId;
    private String favoriteMovieName;
    private Long favoriteMovieId;
    private String gender;
    private String ageGroup;
    private String provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}