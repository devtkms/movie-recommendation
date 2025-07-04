package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.response.TmdbResponse;
import org.springframework.stereotype.Service;

/**
 * Service for searching movies by title using the TMDb API.
 */
@Service
public class MovieSearchService {

    private final TmdbApiClient tmdbApiClient;

    public MovieSearchService(TmdbApiClient tmdbApiClient) {
        this.tmdbApiClient = tmdbApiClient;
    }

    /**
     * Search movies based on the given title.
     *
     * @param title The movie title to search for
     * @return Search result as a TMDb response
     */
    public TmdbResponse searchByTitle(String title) {
        return tmdbApiClient.searchMoviesByTitle(title);
    }
}