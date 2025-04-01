package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;
import com.devtkms.movierecommendation.service.MovieRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
public class MovieRecommendationController {

    private final MovieRecommendationService recommendationService;

    public MovieRecommendationController(MovieRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public ResponseEntity<MovieRecommendationResultDto> getRecommendations(
            @RequestBody MovieRecommendationRequestDto requestDto) {
        MovieRecommendationResultDto response = recommendationService.recommendMovies(requestDto);
        return ResponseEntity.ok(response);
    }
}