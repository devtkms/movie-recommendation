package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.MovieOverviewRequestDto;
import com.devtkms.movierecommendation.dto.MovieOverviewResponseDto;
import com.devtkms.movierecommendation.service.MovieOverviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for retrieving movie overview information.
 */
@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieOverviewController {

    /** Service for retrieving movie overview data */
    private final MovieOverviewService movieOverviewService;

    /**
     * Endpoint to retrieve movie overview information based on the specified movie ID.
     *
     * @param request Request DTO containing the movie ID
     * @return Response DTO containing movie overview information
     */
    @PostMapping("/overview")
    public MovieOverviewResponseDto getMovieOverview(@RequestBody MovieOverviewRequestDto request) {
        return movieOverviewService.getMovieOverview(request.getMovieId());
    }
}
