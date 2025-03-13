package com.devtkms.movierecommendation.dto;

import lombok.Data;

@Data
public class MovieRecommendationRequestDto {
    private String genre;  // 映画ジャンル
    private String provider;  // 配信サービス
    private String language;  // 言語
    private String length;  // 映画の長さ（任意）
    private String year;  // 年代（任意）
}