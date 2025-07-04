package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * DTO for request to save a movie to favorites.
 */
@Data
public class MovieSaveRequestDto {

    /** Unique movie ID (TMDb ID) */
    private Long movieId;

    /** Movie title */
    private String title;

    /** Poster image path (TMDb-based) */
    private String posterPath;
}