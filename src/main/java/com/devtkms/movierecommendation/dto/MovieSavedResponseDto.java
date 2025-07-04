package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for response when a movie is saved to favorites.
 */
@Data
@AllArgsConstructor
public class MovieSavedResponseDto {

    /** Unique movie ID (TMDb ID) */
    private Long movieId;

    /** Movie title */
    private String title;

    /** Poster image path (TMDb-based) */
    private String posterPath;
}