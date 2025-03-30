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

import java.time.LocalDate;
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
        // 配信サービスやジャンルごとにリクエストを送信し、その結果をマージする
        Set<MovieRecommendationResponseDto> allMovies = new HashSet<>();

        // 配信サービスが指定されていればリクエストを送信 (String型なのでそのまま使用)
        String provider = (requestDto.getProvider() != null && !requestDto.getProvider().isEmpty()) ? requestDto.getProvider() : "netflix"; // デフォルト値 "netflix"
        if (provider != null && !provider.isEmpty()) {
            String url = buildUrl(requestDto, provider); // 言語を指定しない
            logger.info("🎬 TMDB API Request: {}", url);
            TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);
            if (response != null) {
                allMovies.addAll(response.toMovieDtoList());
            }
        }

        // 現在の日時を取得
        LocalDate today = LocalDate.now();

        // 上映開始日が過ぎた映画だけを返す
        return allMovies.stream()
                .filter(movie -> movie.getPosterPath() != null && !movie.getPosterPath().isEmpty())
                .filter(movie -> isReleaseDatePassed(movie.getReleaseDate(), today))  // 公開日チェック
                .collect(Collectors.toList());
    }

    private boolean isReleaseDatePassed(String releaseDate, LocalDate today) {
        // releaseDate が null でなければ、公開日を LocalDate 型に変換して比較
        if (releaseDate != null && !releaseDate.isEmpty()) {
            LocalDate release = LocalDate.parse(releaseDate);
            return release.isBefore(today) || release.isEqual(today);  // 公開日が過去日または今日であれば true
        }
        return false;
    }

    private String buildUrl(MovieRecommendationRequestDto requestDto, String provider) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(TMDB_BASE_URL + "/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("sort_by", "popularity.desc")
                .queryParam("page", 1) // 固定ページ番号
                .queryParam("without_genres", "16") // アニメ除外（必要なら削除）
                .queryParam("include_adult", "false") // 成人向け映画を除外

                // 言語パラメータを日本語に設定
                .queryParam("language", "ja"); // 日本語で取得

        // 複数のプロバイダーを指定（OR条件）
        if (provider != null && !provider.isEmpty()) {
            uriBuilder.queryParam("with_watch_providers", "8,9,11,13"); // Netflix、Amazon Prime、Disney Plus、Hulu
        }

        // ジャンルの追加
        if (requestDto.getGenre() != null && !requestDto.getGenre().isEmpty()) {
            uriBuilder.queryParam("with_genres", requestDto.getGenre());
        }

        return uriBuilder.toUriString();
    }
}