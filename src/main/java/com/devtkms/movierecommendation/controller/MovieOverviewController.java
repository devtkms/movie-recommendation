package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.MovieOverviewRequestDto;
import com.devtkms.movierecommendation.dto.MovieOverviewResponseDto;
import com.devtkms.movierecommendation.service.MovieOverviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieOverviewController {

    private final MovieOverviewService movieOverviewService;

    @PostMapping("/overview")
    public MovieOverviewResponseDto getMovieOverview(@RequestBody MovieOverviewRequestDto request) {
        return movieOverviewService.getMovieOverview(request.getMovieId());
    }
}
