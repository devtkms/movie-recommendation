package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.service.MovieRecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieRecommendationController {

    private final MovieRecommendationService movieRecommendationService;

    public MovieRecommendationController(MovieRecommendationService movieRecommendationService) {
        this.movieRecommendationService = movieRecommendationService;
    }

    @PostMapping
    public List<MovieRecommendationResponseDto> recommendMovies(
            @RequestBody MovieRecommendationRequestDto requestDto,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size) { // デフォルトで3件取得
        return movieRecommendationService.getMovies(requestDto, page, size);
    }
}