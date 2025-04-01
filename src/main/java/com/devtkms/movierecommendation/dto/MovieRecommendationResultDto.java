package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MovieRecommendationResultDto {
    private List<MovieRecommendationResponseDto> combined;
}
