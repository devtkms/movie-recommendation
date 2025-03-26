package com.devtkms.movierecommendation.dto;

import lombok.Data;

@Data
public class MovieRecommendationRequestDto {
    private String genre;
    private String provider;
    private String language;
    private boolean includeAnime; // ✅ 追加
}