package com.devtkms.movierecommendation.service.impl;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.response.TmdbResponse;
import com.devtkms.movierecommendation.service.MovieFetchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieFetchServiceImpl implements MovieFetchService {

    private static final Logger logger = LoggerFactory.getLogger(MovieFetchServiceImpl.class);
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3";

    @Value("${TMDB_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<MovieRecommendationResponseDto> fetchMovies(MovieRecommendationRequestDto requestDto) {
        // 配信サービスや言語ごとにリクエストを送信し、その結果をマージする
        Set<MovieRecommendationResponseDto> allMovies = new HashSet<>();

        // 配信サービスごとにリクエストを送信
        for (String provider : requestDto.getProviders()) {
            String url = buildUrl(requestDto, provider, null); // 言語を指定しない
            logger.info("🎬 TMDB API Request: {}", url);
            TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);
            if (response != null) {
                allMovies.addAll(response.toMovieDtoList());
            }
        }

        // 言語ごとにリクエストを送信
        for (String language : requestDto.getLanguages()) {
            String url = buildUrl(requestDto, null, language); // 配信サービスを指定しない
            logger.info("🎬 TMDB API Request: {}", url);
            TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);
            if (response != null) {
                allMovies.addAll(response.toMovieDtoList());
            }
        }

        // 重複した映画を排除し、必要な情報のみを返す
        return allMovies.stream()
                .filter(movie -> movie.getPosterPath() != null && !movie.getPosterPath().isEmpty())
                .collect(Collectors.toList());
    }

    private String buildUrl(MovieRecommendationRequestDto requestDto, String provider, String language) {
        String providersParam = provider != null ? provider : String.join(",", requestDto.getProviders());
        String languagesParam = language != null ? language : String.join(",", requestDto.getLanguages());

        return UriComponentsBuilder.fromHttpUrl(TMDB_BASE_URL + "/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("watch_region", "JP")
                .queryParam("with_watch_providers", providersParam)
                .queryParam("with_genres", requestDto.getGenre())
                .queryParam("with_original_language", languagesParam)
                .queryParam("language", "ja-JP")
                .queryParam("sort_by", "popularity.desc")
                .queryParam("page", new Random().nextInt(2) + 1)
                .queryParam("without_genres", "16") // アニメ除外
                .toUriString();
    }
}