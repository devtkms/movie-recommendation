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

        // 配信サービスが指定されていればリクエストを送信 (String型なのでそのまま使用)
        String provider = (requestDto.getProvider() != null && !requestDto.getProvider().isEmpty()) ? requestDto.getProvider() : "netflix"; // デフォルト値 "netflix"
        if (provider != null && !provider.isEmpty()) {
            String url = buildUrl(requestDto, provider, null); // 言語を指定しない
            logger.info("🎬 TMDB API Request: {}", url);
            TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);
            if (response != null) {
                allMovies.addAll(response.toMovieDtoList());
            }
        }

        // 言語が指定されていればリクエストを送信
        String language = (requestDto.getLanguage() != null && !requestDto.getLanguage().isEmpty()) ? requestDto.getLanguage() : "ja"; // デフォルト値 "ja"
        if (language != null && !language.isEmpty()) {
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
        // 言語を指定する場合は"ja-JP"（日本語）を設定
        String languageParam = (language != null && !language.isEmpty()) ? language : "ja-JP"; // デフォルトを"ja-JP"に設定

        // プロバイダーとランゲージが空であれば省略
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(TMDB_BASE_URL + "/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("sort_by", "popularity.desc")
                .queryParam("page", new Random().nextInt(2) + 1)
                .queryParam("without_genres", "16") // アニメ除外

                // プロバイダーが指定されていれば追加
                .queryParam("watch_region", "JP")
                .queryParam("include_adult", "false"); // 成人向け映画を除外

        if (provider != null && !provider.isEmpty()) {
            uriBuilder.queryParam("with_watch_providers", provider);
        }
        if (language != null && !language.isEmpty()) {
            uriBuilder.queryParam("with_original_language", language);
        }

        // ジャンルの追加
        if (requestDto.getGenre() != null && !requestDto.getGenre().isEmpty()) {
            uriBuilder.queryParam("with_genres", requestDto.getGenre());
        }

        return uriBuilder.toUriString();
    }
}