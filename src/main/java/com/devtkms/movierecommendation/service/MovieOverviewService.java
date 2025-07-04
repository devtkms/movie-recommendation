package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.dto.MovieOverviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service responsible for retrieving movie overviews.
 */
@Service
@RequiredArgsConstructor
public class MovieOverviewService {

    private final TmdbApiClient tmdbApiClient;

    /**
     * Fetch overview information for a specific movie.
     *
     * @param movieId The ID of the movie to fetch
     * @return The overview response
     */
    public MovieOverviewResponseDto getMovieOverview(Long movieId) {
        return tmdbApiClient.fetchMovieOverview(movieId);
    }
}