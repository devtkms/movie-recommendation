package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.service.MovieRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*") // 必要に応じてCORS設定
public class MovieRecommendationController {

    private final MovieRecommendationService recommendationService;

    public MovieRecommendationController(MovieRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public ResponseEntity<Map<String, List<MovieRecommendationResponseDto>>> recommendMovies(
            @RequestBody MovieRecommendationRequestDto requestDto
    ) {
        Map<String, List<MovieRecommendationResponseDto>> result = recommendationService.recommend(requestDto);
        return ResponseEntity.ok(result);
    }
}