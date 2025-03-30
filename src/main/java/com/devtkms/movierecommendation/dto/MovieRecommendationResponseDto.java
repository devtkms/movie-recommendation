package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRecommendationResponseDto {
    private String title;        // 映画タイトル
    private String overview;     // 概要
    private String posterPath;   // ポスター画像のパス
    private String genreId;      // ジャンルID
    private int genreScore;      // ジャンルスコア
    private String releaseDate;  // 公開日（追加）
}