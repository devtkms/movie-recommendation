package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRecommendationResponseDto {
    private Long id;             // ← ✅ 映画IDを追加
    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;
}