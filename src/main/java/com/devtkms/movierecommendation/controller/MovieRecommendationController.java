package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.service.MovieRecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // ← 追加

@RestController
@RequestMapping("/api/movies")
public class MovieRecommendationController {

    private final MovieRecommendationService movieRecommendationService;

    public MovieRecommendationController(MovieRecommendationService movieRecommendationService) {
        this.movieRecommendationService = movieRecommendationService;
    }

    @PostMapping
    public Map<String, List<MovieRecommendationResponseDto>> recommendMovies(
            @RequestBody MovieRecommendationRequestDto requestDto) {
        return movieRecommendationService.getMovies(requestDto);
    }
}