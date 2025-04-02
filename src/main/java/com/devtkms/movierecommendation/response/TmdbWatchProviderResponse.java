package com.devtkms.movierecommendation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * TMDb APIの「Watch Providers（配信サービス情報）」レスポンスを受け取るモデル
 *
 * 例：https://api.themoviedb.org/3/movie/{movie_id}/watch/providers
 */
@Data
public class TmdbWatchProviderResponse {

    /**
     * 国コード（例: "JP", "US"）ごとの配信情報
     * Key: 国コード
     * Value: 各国の配信サービス情報
     */
    @JsonProperty("results")
    private Map<String, CountryWatchInfo> results;

    /**
     * 各国における視聴可能サービス情報
     */
    @Data
    public static class CountryWatchInfo {

        /**
         * 定額配信で視聴可能なサービス一覧（例: Netflix, Prime Video）
         */
        private List<Provider> flatrate;

        /**
         * 配信プロバイダー（サービス）情報
         */
        @Data
        public static class Provider {

            /** プロバイダー名（例: "Netflix", "U-NEXT"） */
            @JsonProperty("provider_name")
            private String providerName;

            /** プロバイダーのロゴ画像パス（例: "/abc123.png"） */
            @JsonProperty("logo_path")
            private String logoPath;
        }
    }
}