package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.dto.MovieOverviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieOverviewService {

    private final TmdbApiClient tmdbApiClient;

    public MovieOverviewResponseDto getMovieOverview(Long movieId) {
        return tmdbApiClient.fetchMovieOverview(movieId);
    }
}