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

    // APIレスポンスをDTOリストに変換するメソッド
    public List<MovieRecommendationResponseDto> toMovieDtoList() {
        return results.stream().map(result -> new MovieRecommendationResponseDto(
                result.getTitle(),
                result.getOverview(),
                result.getPosterPath()
        )).collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class MovieResult {
        private String title;
        private String overview;

        @JsonProperty("poster_path")
        private String posterPath;
    }
}
