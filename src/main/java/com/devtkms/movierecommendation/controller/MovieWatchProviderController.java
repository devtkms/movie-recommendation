package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.WatchProviderResponseDto;
import com.devtkms.movierecommendation.service.MovieWatchProviderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieWatchProviderController {

    private final MovieWatchProviderService providerService;

    public MovieWatchProviderController(MovieWatchProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping("/{movieId}/watch/providers")
    public List<WatchProviderResponseDto> getWatchProviders(@PathVariable Long movieId) {
        return providerService.getWatchProviders(movieId);
    }
}