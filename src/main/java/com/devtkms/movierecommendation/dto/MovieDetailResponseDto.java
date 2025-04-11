package com.devtkms.movierecommendation.dto;

import com.devtkms.movierecommendation.response.TmdbWatchProviderResponse;
import lombok.Data;

import java.util.List;

@Data
public class MovieDetailResponseDto {
    private MovieOverviewResponseDto overview;
    private List<WatchProviderResponseDto> providers;
}
