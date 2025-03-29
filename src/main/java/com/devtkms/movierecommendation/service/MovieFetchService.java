package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;

import java.util.List;

public interface MovieFetchService {
    List<MovieRecommendationResponseDto> fetchMovies(MovieRecommendationRequestDto requestDto);
}