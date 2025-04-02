package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 映画推薦結果を保持するDTO（複数作品を含む）
 */
@Data
@AllArgsConstructor
public class MovieRecommendationResultDto {

    /** 推薦された映画のリスト（最大20件想定） */
    private List<MovieRecommendationResponseDto> combined;
}