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

        // 🎲 ランダムに 1〜5ページ目から1つ、6〜10ページ目から1つ選ぶ
        int page1 = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, 6);  // 1〜5
        int page2 = java.util.concurrent.ThreadLocalRandom.current().nextInt(6, 11); // 6〜10

        for (int page : List.of(page1, page2)) {
            String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/discover/movie")
                    .queryParam("api_key", apiKey)
                    .queryParam("with_keywords", keywordParam)
                    .queryParam("language", "ja-JP")
                    .queryParam("sort_by", "popularity.desc")
                    .queryParam("without_genres", "16")
                    .queryParam("page", page)
                    .build()
                    .toUriString();

            logger.info("🎲 TMDb ランダムページ取得 (page=" + page + "): " + url);

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