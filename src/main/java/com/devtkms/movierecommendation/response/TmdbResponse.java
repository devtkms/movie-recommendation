package com.devtkms.movierecommendation.response;

import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for mapping the response from TMDb's movie search API.
 */
@Getter
@Setter
public class TmdbResponse {

    /**
     * List of movie data returned by TMDb API.
     */
    @JsonProperty("results")
    private List<MovieResult> results;

    /**
     * Converts the TMDb response into a list of application-specific DTOs.
     *
     * Only movies with poster images are included.
     *
     * @return List of MovieRecommendationResponseDto.
     */
    public List<MovieRecommendationResponseDto> toMovieDtoList() {
        if (results == null) return List.of();

        return results.stream()
                // Exclude movies without poster images (needed for UI display)
                .filter(result -> result.getPosterPath() != null && !result.getPosterPath().isEmpty())
                .map(result -> new MovieRecommendationResponseDto(
                        result.getId(),          // Movie ID
                        result.getTitle(),       // Title
                        result.getOverview(),    // Overview
                        result.getPosterPath(),  // Poster image path
                        result.getReleaseDate(), // Release date
                        false
                ))
                .collect(Collectors.toList());
    }

    /**
     * Inner class representing each movie result in the TMDb API response.
     */
    @Getter
    @Setter
    public static class MovieResult {

        private Long id;           // Unique movie ID on TMDb
        private String title;      // Title
        private String overview;   // Overview

        @JsonProperty("poster_path")
        private String posterPath; // Poster image path

        @JsonProperty("genre_ids")
        private List<Integer> genreIds; // List of genre IDs (optional)

        @JsonProperty("release_date")
        private String releaseDate; // Release date
    }
}