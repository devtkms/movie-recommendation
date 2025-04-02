package com.devtkms.movierecommendation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TmdbWatchProviderResponse {

    @JsonProperty("results")
    private Map<String, CountryWatchInfo> results;

    @Data
    public static class CountryWatchInfo {
        private List<Provider> flatrate;

        @Data
        public static class Provider {
            @JsonProperty("provider_name")
            private String providerName;

            @JsonProperty("logo_path")
            private String logoPath;
        }
    }
}