package com.devtkms.movierecommendation.client;

import com.devtkms.movierecommendation.response.TmdbResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class TmdbApiClient {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public TmdbApiClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public TmdbResponse fetchMoviesByKeywords(List<String> keywordIds) {
        String keywordParam = String.join(",", keywordIds);
        String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("with_keywords", keywordParam)
                .queryParam("language", "ja-JP") // 日本語
                .queryParam("sort_by", "popularity.desc")
                .build()
                .toUriString();

        ResponseEntity<TmdbResponse> response = restTemplate.getForEntity(url, TmdbResponse.class);
        return response.getBody();
    }
}
