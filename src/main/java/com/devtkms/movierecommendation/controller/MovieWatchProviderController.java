package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.WatchProviderResponseDto;
import com.devtkms.movierecommendation.service.MovieWatchProviderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller that returns watch providers (streaming services) for a given movie.
 */
@RestController
@RequestMapping("/movie")
public class MovieWatchProviderController {

    private final MovieWatchProviderService providerService;

    public MovieWatchProviderController(MovieWatchProviderService providerService) {
        this.providerService = providerService;
    }

    /**
     * Retrieve a list of streaming services available in Japan for the specified movie ID.
     *
     * @param movieId TMDb movie ID
     * @return List of DTOs for available watch providers (e.g., Netflix, Prime Video)
     */
    @GetMapping("/{movieId}/watch/providers")
    public List<WatchProviderResponseDto> getWatchProviders(@PathVariable Long movieId) {
        return providerService.getWatchProviders(movieId);
    }
}