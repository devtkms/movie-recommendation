package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.dto.WatchProviderResponseDto;
import com.devtkms.movierecommendation.response.TmdbWatchProviderResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Service for retrieving and formatting watch provider information for a given movie.
 */
@Service
public class MovieWatchProviderService {

    private final TmdbApiClient tmdbApiClient;

    public MovieWatchProviderService(TmdbApiClient tmdbApiClient) {
        this.tmdbApiClient = tmdbApiClient;
    }

    /**
     * Retrieves watch providers available in Japan for a specific movie.
     *
     * @param movieId The TMDb movie ID
     * @return List of selected watch providers with logo paths
     */
    public List<WatchProviderResponseDto> getWatchProviders(Long movieId) {
        TmdbWatchProviderResponse response = tmdbApiClient.fetchWatchProviders(movieId);

        var jp = response.getResults().get("JP");
        if (jp == null || jp.getFlatrate() == null) return Collections.emptyList();

        List<String> allowedProviders = List.of(
                "Amazon Prime Video",
                "Netflix",
                "Hulu",
                "U-NEXT",
                "Disney Plus"
        );

        return jp.getFlatrate().stream()
                .filter(p -> allowedProviders.contains(p.getProviderName()))
                .map(p -> new WatchProviderResponseDto(p.getProviderName(), p.getLogoPath()))
                .toList();
    }
}