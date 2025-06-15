package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;

public interface MovieRecommendationService {

    MovieRecommendationResultDto recommendMovies(MovieRecommendationRequestDto requestDto, String userId);

    MovieRecommendationResultDto getPersonalizeMovies(Long userId, String usrId);
}