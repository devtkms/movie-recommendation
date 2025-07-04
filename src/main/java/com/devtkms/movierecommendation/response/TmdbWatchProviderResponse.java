package com.devtkms.movierecommendation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Model for receiving response from TMDb's "Watch Providers" API.
 *
 * Example: https://api.themoviedb.org/3/movie/{movie_id}/watch/providers
 */
@Data
public class TmdbWatchProviderResponse {

    /**
     * Map of watch providers by country code (e.g., "JP", "US").
     * Key: Country code
     * Value: Watch provider info for that country
     */
    @JsonProperty("results")
    private Map<String, CountryWatchInfo> results;

    /**
     * Watch provider info for each country.
     */
    @Data
    public static class CountryWatchInfo {

        /**
         * List of services available via subscription (e.g., Netflix, Prime Video).
         */
        private List<Provider> flatrate;

        /**
         * Information for each provider (streaming service).
         */
        @Data
        public static class Provider {

            /** Provider name (e.g., "Netflix", "U-NEXT") */
            @JsonProperty("provider_name")
            private String providerName;

            /** Path to the provider's logo image (e.g., "/abc123.png") */
            @JsonProperty("logo_path")
            private String logoPath;
        }
    }
}