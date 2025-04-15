package com.devtkms.movierecommendation.response;

import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TMDbの映画検索APIのレスポンスをマッピングするクラス
 */
@Getter
@Setter
public class TmdbResponse {

    /**
     * TMDb APIから返却される映画情報リスト
     */
    @JsonProperty("results")
    private List<MovieResult> results;

    /**
     * TMDbレスポンスをアプリケーション用DTOリストに変換する
     *
     * ポスター画像が存在する映画のみを対象とする。
     *
     * @return MovieRecommendationResponseDtoのリスト
     */
    public List<MovieRecommendationResponseDto> toMovieDtoList() {
        if (results == null) return List.of();

        return results.stream()
                // ポスター画像がない映画は除外（UI表示に支障が出るため）
                .filter(result -> result.getPosterPath() != null && !result.getPosterPath().isEmpty())
                .map(result -> new MovieRecommendationResponseDto(
                        result.getId(),         // 映画ID
                        result.getTitle(),      // タイトル
                        result.getOverview(),   // 概要
                        result.getPosterPath(), // ポスター画像パス
                        result.getReleaseDate(), // 公開日
                        false
                ))
                .collect(Collectors.toList());
    }

    /**
     * TMDb APIの各映画結果を表す内部クラス
     */
    @Getter
    @Setter
    public static class MovieResult {

        private Long id;          // 映画ID（TMDb上の一意ID）
        private String title;     // タイトル
        private String overview;  // 概要

        @JsonProperty("poster_path")
        private String posterPath; // ポスター画像パス

        @JsonProperty("genre_ids")
        private List<Integer> genreIds; // ジャンルIDのリスト（未使用なら削除も検討）

        @JsonProperty("release_date")
        private String releaseDate; // 公開日
    }
}