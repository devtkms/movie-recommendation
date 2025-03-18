package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRecommendationResponseDto {
    private Long id;          // 映画ID
    private String title;      // 映画タイトル
    private String overview;   // 概要
    private String posterPath; // ポスター画像のパス

    // 🔥 追加: id なしのコンストラクター
    public MovieRecommendationResponseDto(String title, String overview, String posterPath) {
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
    }
}