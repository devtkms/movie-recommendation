package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.dto.WatchProviderResponseDto;
import com.devtkms.movierecommendation.response.TmdbWatchProviderResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieWatchProviderService {

    private final TmdbApiClient tmdbApiClient;

    public MovieWatchProviderService(TmdbApiClient tmdbApiClient) {
        this.tmdbApiClient = tmdbApiClient;
    }

    public List<WatchProviderResponseDto> getWatchProviders(Long movieId) {
        TmdbWatchProviderResponse response = tmdbApiClient.fetchWatchProviders(movieId);
        var jp = response.getResults().get("JP");

        if (jp == null || jp.getFlatrate() == null) return Collections.emptyList();

        List<String> allowedProviders = List.of(
                "Amazon Prime Video",
                "Netflix",
                "Hulu",
                "U-NEXT",
                "Disney Plus"  // ← ディズニープラス追加
        );

        return jp.getFlatrate().stream()
                .filter(p -> allowedProviders.contains(p.getProviderName()))
                .map(p -> new WatchProviderResponseDto(p.getProviderName(), p.getLogoPath()))
                .toList();
    }
}