package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;

/**
 * Interface for movie recommendation service.
 */
public interface MovieRecommendationService {

    /**
     * Recommend movies based on user's answers.
     *
     * @param requestDto Questionnaire answers (mood, tone, after)
     * @param userId Optional user ID
     * @return List of recommended movies
     */
    MovieRecommendationResultDto recommendMovies(MovieRecommendationRequestDto requestDto, String userId);

    /**
     * Recommend movies personalized based on the user's favorite movie.
     *
     * @param userId User's database ID
     * @param usrId  User's UUID
     * @return Personalized movie recommendations
     */
    MovieRecommendationResultDto getPersonalizeMovies(Long userId, String usrId);
}