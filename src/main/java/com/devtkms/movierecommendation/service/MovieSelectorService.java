package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;

import java.util.List;
import java.util.Map;

public interface MovieSelectorService {
    List<MovieRecommendationResponseDto> selectTopMovies(
            Map<String, Integer> genreScores,
            List<MovieRecommendationResponseDto> allMovies,
            int limit
    );
}