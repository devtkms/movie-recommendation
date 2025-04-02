package com.devtkms.movierecommendation.client;

import com.devtkms.movierecommendation.response.TmdbResponse;
import com.devtkms.movierecommendation.response.TmdbWatchProviderResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.logging.Logger;

@Component
public class TmdbApiClient {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private static final Logger logger = Logger.getLogger(TmdbApiClient.class.getName());

    public TmdbApiClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public TmdbResponse fetchMoviesByKeywords(List<String> keywordIds) {
        String keywordParam = String.join(",", keywordIds);
        List<TmdbResponse.MovieResult> allResults = new java.util.ArrayList<>();

        for (int page = 1; page <= 2; page++) {
            String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/discover/movie")
                    .queryParam("api_key", apiKey)
                    .queryParam("with_keywords", keywordParam)
                    .queryParam("language", "ja-JP")
                    .queryParam("sort_by", "popularity.desc")
                    .queryParam("without_genres", "16")
                    .queryParam("page", page) // ✅ 明示的にページ指定
                    .build()
                    .toUriString();

            logger.info("🔗 TMDb API リクエストURL (page=" + page + "): " + url);

            ResponseEntity<TmdbResponse> response = restTemplate.getForEntity(url, TmdbResponse.class);
            if (response.getBody() != null && response.getBody().getResults() != null) {
                allResults.addAll(response.getBody().getResults());
            }
        }

        TmdbResponse mergedResponse = new TmdbResponse();
        mergedResponse.setResults(allResults);
        return mergedResponse;
    }

    public TmdbWatchProviderResponse fetchWatchProviders(Long movieId) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/movie/" + movieId + "/watch/providers")
                .queryParam("api_key", apiKey)
                .build()
                .toUriString();

        logger.info("📺 TMDb 配信情報 URL: " + url);

        ResponseEntity<TmdbWatchProviderResponse> response =
                restTemplate.getForEntity(url, TmdbWatchProviderResponse.class);
        return response.getBody();
    }
}