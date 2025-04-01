package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieSelectorService {
    public List<MovieRecommendationResponseDto> selectUniqueMovies(List<MovieRecommendationResponseDto> movies, int limit) {
        return movies.stream()
                .distinct()
                .limit(limit)
                .toList();
    }
}