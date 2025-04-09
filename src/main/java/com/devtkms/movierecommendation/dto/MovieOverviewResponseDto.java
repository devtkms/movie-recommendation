package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MovieOverviewResponseDto {
    private String overview;
    private String releaseDate;
    private Integer runtime;
    private List<String> productionCountries;
}