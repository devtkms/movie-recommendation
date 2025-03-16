package com.devtkms.movierecommendation.dto;

import lombok.Data;

@Data
public class MovieRecommendationRequestDto {
    private String genre;  // 映画ジャンル
    private String provider;  // 配信サービス
    private String language;  // 言語
}