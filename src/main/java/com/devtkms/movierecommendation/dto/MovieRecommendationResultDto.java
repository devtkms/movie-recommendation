package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO holding the final list of recommended movies.
 */
@Data
@AllArgsConstructor
public class MovieRecommendationResultDto {

    /** List of recommended movies (up to 20 expected) */
    private List<MovieRecommendationResponseDto> combined;
}