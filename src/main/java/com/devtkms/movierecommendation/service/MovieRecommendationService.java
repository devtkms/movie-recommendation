package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.response.TmdbResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
     * ユーザーの検索条件に基づいて、異なるタイプの映画（評価順、人気順、新作、ランダム）を最大40件取得する。
     * 各カテゴリで15件ずつ取得し、重複を除外して最大40件まで絞り込む。
     */
    @Cacheable(value = "movies", key = "#requestDto.genre + '_' + #requestDto.provider + '_' + #requestDto.language", unless = "#result == null or #result.isEmpty()")
    public Map<String, List<MovieRecommendationResponseDto>> getMovies(MovieRecommendationRequestDto requestDto) {
        Map<String, List<MovieRecommendationResponseDto>> categorizedMovies = new HashMap<>();
        Set<String> seenTitles = new HashSet<>();

        List<MovieRecommendationResponseDto> highRated = fetchMoviesFromTmdb("/discover/movie", requestDto, 15, "vote_average.desc", 1);
        List<MovieRecommendationResponseDto> popular = fetchMoviesFromTmdb("/discover/movie", requestDto, 15, "popularity.desc", 1);
        List<MovieRecommendationResponseDto> recent = fetchMoviesFromTmdb("/discover/movie", requestDto, 15, "release_date.desc", 1);
        int randomPage = new Random().nextInt(2) + 2; // 2 or 3
        List<MovieRecommendationResponseDto> surprise = fetchMoviesFromTmdb("/discover/movie", requestDto, 15, "popularity.desc", randomPage);

        // 重複を除きながらカテゴリごとに追加（40件を上限）
        List<MovieRecommendationResponseDto> allMovies = new ArrayList<>();

        for (MovieRecommendationResponseDto movie : mergeLists(highRated, popular, recent, surprise)) {
            if (!seenTitles.contains(movie.getTitle())) {
                allMovies.add(movie);
                seenTitles.add(movie.getTitle());
            }
            if (allMovies.size() >= 40) break;
        }

        categorizedMovies.put("combined", allMovies);
        return categorizedMovies;
    }

    /**
     * TMDb API から映画情報を取得する共通メソッド。
     * @param endpoint APIエンドポイント
     * @param requestDto 検索条件（ジャンル、配信サービス、言語）
     * @param limit 最大取得数
     * @param sortBy ソート条件（例：popularity.desc）
     * @param page APIのページ番号
     */
    private List<MovieRecommendationResponseDto> fetchMoviesFromTmdb(String endpoint, MovieRecommendationRequestDto requestDto, int limit, String sortBy, int page) {
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
                .filter(movie -> movie.getPosterPath() != null && !movie.getPosterPath().isEmpty())
                .limit(limit)
                .collect(Collectors.toList())
                : List.of();
    }

    /**
     * 複数の映画リストを順番に結合するヘルパーメソッド。
     */
    private List<MovieRecommendationResponseDto> mergeLists(List<MovieRecommendationResponseDto>... lists) {
        List<MovieRecommendationResponseDto> merged = new ArrayList<>();
        for (List<MovieRecommendationResponseDto> list : lists) {
            merged.addAll(list);
        }
        return merged;
    }
}