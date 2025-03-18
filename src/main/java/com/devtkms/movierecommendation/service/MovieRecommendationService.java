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

    @Cacheable(value = "movies", key = "#requestDto.genre + '_' + #requestDto.provider + '_' + #requestDto.language",
            unless = "#result == null or #result.isEmpty()")
    public Map<String, List<MovieRecommendationResponseDto>> getMovies(MovieRecommendationRequestDto requestDto) {
        Map<String, List<MovieRecommendationResponseDto>> categorizedMovies = new HashMap<>();

        // 🔥 まずはトレンド映画を取得（20件）
        List<MovieRecommendationResponseDto> trendMovies = fetchMoviesFromTmdb("/discover/movie", requestDto, 10, "popularity.desc");
        categorizedMovies.put("trend", trendMovies);

        // 🔥 トレンドに含まれている映画のタイトルをセットで取得
        Set<String> trendMovieTitles = trendMovies.stream()
                .map(MovieRecommendationResponseDto::getTitle)
                .collect(Collectors.toSet());

        // 🔥 名作（高評価作品）を取得（最初のページ）
        List<MovieRecommendationResponseDto> topRatedMovies = fetchMoviesFromTmdb("/discover/movie", requestDto, 20, "vote_average.desc")
                .stream()
                .filter(movie -> !trendMovieTitles.contains(movie.getTitle())) // 🔥 トレンドにある映画を除外
                .collect(Collectors.toList());

        // 🔥 不足分を追加で取得（最大20件になるように）
        int missingCount = 20 - topRatedMovies.size();
        if (missingCount > 0) {
            logger.info("📉 `toprated` が {}件不足。追加取得を実行...", missingCount);
            topRatedMovies.addAll(fetchAdditionalTopRatedMovies(requestDto, trendMovieTitles, missingCount));
        }

        categorizedMovies.put("toprated", topRatedMovies);

        return categorizedMovies;
    }

    private List<MovieRecommendationResponseDto> fetchMoviesFromTmdb(String endpoint, MovieRecommendationRequestDto requestDto,
                                                                     int limit, String sortBy) {
        return fetchMoviesFromTmdb(endpoint, requestDto, limit, sortBy, 1); // 1ページ目から取得
    }

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
                .queryParam("page", page);  // 🔥 ページ指定

        String url = urlBuilder.toUriString();
        logger.info("📡 TMDb APIリクエスト: {}", url);

        // TMDb APIからデータを取得
        TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);

        // 取得結果から最大 limit 件を取得
        return response != null ? response.toMovieDtoList().stream().limit(limit).collect(Collectors.toList()) : List.of();
    }

    private List<MovieRecommendationResponseDto> fetchAdditionalTopRatedMovies(MovieRecommendationRequestDto requestDto,
                                                                               Set<String> trendMovieTitles,
                                                                               int neededCount) {
        List<MovieRecommendationResponseDto> additionalMovies = new ArrayList<>();
        int page = 2; // 🔥 追加取得する場合は2ページ目から

        while (additionalMovies.size() < neededCount) {
            List<MovieRecommendationResponseDto> movies = fetchMoviesFromTmdb("/discover/movie", requestDto, neededCount, "vote_average.desc", page);
            if (movies.isEmpty()) {
                break; // これ以上映画がない場合は終了
            }

            // トレンドにある映画を除外しつつ追加
            List<MovieRecommendationResponseDto> filteredMovies = movies.stream()
                    .filter(movie -> !trendMovieTitles.contains(movie.getTitle()))
                    .collect(Collectors.toList());

            additionalMovies.addAll(filteredMovies);
            page++; // 次のページを取得
        }

        return additionalMovies.stream().limit(neededCount).collect(Collectors.toList()); // 必要な数だけ取得
    }
}