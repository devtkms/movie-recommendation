package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a single recommended movie returned from TMDb.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRecommendationResponseDto {

    /** Unique movie ID (from TMDb) */
    private Long id;

    /** Movie title */
    private String title;

    /** Movie overview */
    private String overview;

    /** Poster image path */
    private String posterPath;

    /** Release date */
    private String releaseDate;

    /** Whether the movie is saved by the logged-in user */
    private Boolean isSaved; // default: false
}