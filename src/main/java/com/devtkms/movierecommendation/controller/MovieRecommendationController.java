package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;
import com.devtkms.movierecommendation.entity.UserEntity;
import com.devtkms.movierecommendation.service.impl.MovieRecommendationServiceImpl;
import com.devtkms.movierecommendation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling movie recommendation requests.
 */
@RestController
@RequestMapping("/api/recommendations")
public class MovieRecommendationController {

    private final MovieRecommendationServiceImpl recommendationService;
    private final UserService userService;

    public MovieRecommendationController(MovieRecommendationServiceImpl recommendationService, UserService userService) {
        this.recommendationService = recommendationService;
        this.userService = userService;
    }

    /**
     * Endpoint to get recommended movies.
     *
     * Based on 3 questions answered by the user from the frontend,
     * returns a list of recommended movies.
     *
     * @param requestDto User's responses to questions (mood, tone, after)
     * @param authentication Authentication object containing user info
     * @return DTO containing a list of recommended movies
     */
    @PostMapping
    public ResponseEntity<MovieRecommendationResultDto> getRecommendations(
            @RequestBody MovieRecommendationRequestDto requestDto,
            Authentication authentication
    ) {
        String userId = (authentication != null) ? authentication.getName() : null;
        MovieRecommendationResultDto response = recommendationService.recommendMovies(requestDto, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to get personalized trending movies.
     *
     * Returns trending movies personalized to the user
     * without requiring any question responses.
     *
     * @param authentication Authentication object containing user info
     * @return DTO containing a list of trending movies
     */
    @GetMapping("/personalize")
    public ResponseEntity<MovieRecommendationResultDto> getPersonalizeMovies(Authentication authentication) {
        String userId = authentication.getName();
        UserEntity user = userService.findByUserId(userId);
        MovieRecommendationResultDto response = recommendationService.getPersonalizeMovies(user.getId(), userId);
        return ResponseEntity.ok(response);
    }
}