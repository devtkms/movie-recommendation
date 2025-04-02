package com.devtkms.movierecommendation.response;

import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TmdbResponse {

    @JsonProperty("results")
    private List<MovieResult> results;

    public List<MovieRecommendationResponseDto> toMovieDtoList() {
        if (results == null) return List.of();

        return results.stream()
                .filter(result -> result.getPosterPath() != null && !result.getPosterPath().isEmpty())
                .map(result -> new MovieRecommendationResponseDto(
                        result.getId(),               // ✅ IDを追加
                        result.getTitle(),
                        result.getOverview(),
                        result.getPosterPath(),
                        result.getReleaseDate()
                ))
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class MovieResult {
        private Long id;  // ✅ 映画ID追加

        private String title;
        private String overview;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("genre_ids")
        private List<Integer> genreIds;

        @JsonProperty("release_date")
        private String releaseDate;
    }
}