package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * DTO for requesting watch provider information for a movie.
 */
@Data
public class WatchProviderRequestDto {

    /** Target movie ID (TMDb ID) */
    private Long movieId;
}