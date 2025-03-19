package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.response.TmdbResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieRecommendationService {

    @Value("${TMDB_API_KEY}")
    private String apiKey;

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3";
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(MovieRecommendationService.class);

    /**
     * ユーザーのリクエストに基づいて映画の推薦リストを取得する
     * - 「今話題の映画」(トレンド) と「名作映画」(高評価) の2種類を取得
     * - キャッシュを利用し、同じ条件でのAPIリクエストを削減
     *
     * @param requestDto ユーザーのリクエスト情報（ジャンル、配信サービス、言語）
     * @return トレンド映画・名作映画を分類したマップ
     */
    @Cacheable(value = "movies", key = "#requestDto.genre + '_' + #requestDto.provider + '_' + #requestDto.language",
            unless = "#result == null or #result.isEmpty()")
    public Map<String, List<MovieRecommendationResponseDto>> getMovies(MovieRecommendationRequestDto requestDto) {
        Map<String, List<MovieRecommendationResponseDto>> categorizedMovies = new HashMap<>();

        // 🔥 トレンド映画（人気順）
        List<MovieRecommendationResponseDto> trendMovies = fetchMoviesFromTmdb("/discover/movie", requestDto, 10, "popularity.desc");
        categorizedMovies.put("trend", trendMovies);

        // 🔥 トレンド映画のタイトルセット（名作リストから重複を防ぐため）
        Set<String> trendMovieTitles = trendMovies.stream()
                .map(MovieRecommendationResponseDto::getTitle)
                .collect(Collectors.toSet());

        // 🔥 名作映画（高評価順）
        List<MovieRecommendationResponseDto> topRatedMovies = fetchMoviesFromTmdb("/discover/movie", requestDto, 20, "vote_average.desc")
                .stream()
                .filter(movie -> !trendMovieTitles.contains(movie.getTitle())) // 🔥 トレンドにある映画を除外
                .collect(Collectors.toList());

        // 🔥 名作映画が不足している場合、追加取得
        int missingCount = 20 - topRatedMovies.size();
        if (missingCount > 0) {
            logger.info("📉 `toprated` が {}件不足。追加取得を実行...", missingCount);
            topRatedMovies.addAll(fetchAdditionalTopRatedMovies(requestDto, trendMovieTitles, missingCount));
        }

        categorizedMovies.put("toprated", topRatedMovies);

        return categorizedMovies;
    }

    /**
     * TMDb API から映画データを取得（デフォルト1ページ目）
     *
     * @param endpoint APIのエンドポイント（`/discover/movie` など）
     * @param requestDto ユーザーのリクエスト情報
     * @param limit 最大取得件数
     * @param sortBy ソート条件（`popularity.desc` など）
     * @return 取得した映画のリスト
     */
    private List<MovieRecommendationResponseDto> fetchMoviesFromTmdb(String endpoint, MovieRecommendationRequestDto requestDto,
                                                                     int limit, String sortBy) {
        return fetchMoviesFromTmdb(endpoint, requestDto, limit, sortBy, 1); // 1ページ目から取得
    }

    /**
     * TMDb API から映画データを取得
     *
     * @param endpoint APIのエンドポイント
     * @param requestDto ユーザーのリクエスト情報
     * @param limit 最大取得件数
     * @param sortBy ソート条件
     * @param page 取得するページ番号
     * @return 取得した映画のリスト
     */
    private List<MovieRecommendationResponseDto> fetchMoviesFromTmdb(String endpoint, MovieRecommendationRequestDto requestDto,
                                                                     int limit, String sortBy, int page) {
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(TMDB_BASE_URL + endpoint)
                .queryParam("api_key", apiKey)
                .queryParam("watch_region", "JP")
                .queryParam("with_watch_providers", requestDto.getProvider())
                .queryParam("with_genres", requestDto.getGenre())
                .queryParam("with_original_language", requestDto.getLanguage())
                .queryParam("language", "ja-JP")
                .queryParam("sort_by", sortBy)
                .queryParam("page", page);

        String url = urlBuilder.toUriString();
        logger.info("📡 TMDb APIリクエスト: {}", url);

        TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);

        return response != null
                ? response.toMovieDtoList().stream()
                .filter(movie -> movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) // 🔥 ポスターがある映画のみ取得
                .limit(limit)
                .collect(Collectors.toList())
                : List.of();
    }

    /**
     * 追加の名作映画を取得（不足分を補う）
     *
     * @param requestDto ユーザーのリクエスト情報
     * @param trendMovieTitles トレンド映画のタイトル（重複防止用）
     * @param neededCount 追加で必要な映画の数
     * @return 追加取得した映画のリスト
     */
    private List<MovieRecommendationResponseDto> fetchAdditionalTopRatedMovies(MovieRecommendationRequestDto requestDto,
                                                                               Set<String> trendMovieTitles,
                                                                               int neededCount) {
        List<MovieRecommendationResponseDto> additionalMovies = new ArrayList<>();
        int page = 2;

        while (additionalMovies.size() < neededCount) {
            List<MovieRecommendationResponseDto> movies = fetchMoviesFromTmdb("/discover/movie", requestDto, neededCount, "vote_average.desc", page);
            if (movies.isEmpty()) {
                break;
            }

            List<MovieRecommendationResponseDto> filteredMovies = movies.stream()
                    .filter(movie -> !trendMovieTitles.contains(movie.getTitle())) // 🔥 トレンドの映画を除外
                    .filter(movie -> movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) // 🔥 ポスターがある映画のみ
                    .collect(Collectors.toList());

            additionalMovies.addAll(filteredMovies);
            page++;
        }

        return additionalMovies.stream().limit(neededCount).collect(Collectors.toList());
    }
}