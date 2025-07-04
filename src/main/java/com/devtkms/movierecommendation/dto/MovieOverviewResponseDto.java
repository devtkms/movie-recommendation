package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO representing movie overview details.
 */
@Data
@AllArgsConstructor
public class MovieOverviewResponseDto {

    /** Brief description of the movie */
    private String overview;

    /** Release date (format: yyyy-MM-dd) */
    private String releaseDate;

    /** Runtime in minutes */
    private Integer runtime;

    /** List of production countries */
    private List<String> productionCountries;
}