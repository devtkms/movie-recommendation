package com.devtkms.movierecommendation.dto;

import com.devtkms.movierecommendation.response.TmdbWatchProviderResponse;
import lombok.Data;

import java.util.List;

/**
 * DTO that contains detailed movie information including overview and available streaming providers.
 */
@Data
public class MovieDetailResponseDto {

    /** Movie overview details */
    private MovieOverviewResponseDto overview;

    /** List of watch providers (e.g., Netflix, Prime Video) */
    private List<WatchProviderResponseDto> providers;
}