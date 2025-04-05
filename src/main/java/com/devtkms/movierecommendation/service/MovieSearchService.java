package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.response.TmdbResponse;
import org.springframework.stereotype.Service;

@Service
public class MovieSearchService {

    private final TmdbApiClient tmdbApiClient;

    public MovieSearchService(TmdbApiClient tmdbApiClient) {
        this.tmdbApiClient = tmdbApiClient;
    }

    public TmdbResponse searchByTitle(String title) {
        return tmdbApiClient.searchMoviesByTitle(title);
    }
}