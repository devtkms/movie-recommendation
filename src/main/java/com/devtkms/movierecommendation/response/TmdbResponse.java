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
        if (results == null) return List.of();

        return results.stream()
                .filter(result -> result.getPosterPath() != null && !result.getPosterPath().isEmpty()) // ポスターが空でない映画を選択
                .map(result -> {
                    // genreIdsが存在すれば最初のジャンルIDを使う。なければ空文字
                    String genreId = (result.getGenreIds() != null && !result.getGenreIds().isEmpty())
                            ? String.valueOf(result.getGenreIds().get(0)) : "";
                    // MovieRecommendationResponseDto のインスタンスを返す
                    return new MovieRecommendationResponseDto(
                            result.getTitle(),
                            result.getOverview(),
                            result.getPosterPath(),
                            genreId,  // 取得した genreId を設定
                            0,         // genreScore の初期値として0を設定（必要に応じて後で更新）
                            result.getReleaseDate()  // 追加：公開日を設定
                    );
                })
                .collect(Collectors.toList()); // DTOリストを返す
    }

    @Getter
    @Setter
    private static class MovieResult {
        private String title;
        private String overview;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("genre_ids")
        private List<Integer> genreIds; // TMDb APIからのジャンルIDリスト

        @JsonProperty("release_date")
        private String releaseDate; // 追加：公開日
    }
}