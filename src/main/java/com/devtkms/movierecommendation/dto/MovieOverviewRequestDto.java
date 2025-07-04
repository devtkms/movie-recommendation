package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * DTO to request movie overview by movie ID.
 */
@Data
public class MovieOverviewRequestDto {

    /** TMDb movie ID */
    private Long movieId;
}