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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        // 🔥 トレンド映画を取得（10件）
        categorizedMovies.put("trend", fetchMoviesFromTmdb("/discover/movie", requestDto, 10, "popularity.desc"));

        // 🔥 名作（高評価作品）を取得（20件）
        categorizedMovies.put("toprated", fetchMoviesFromTmdb("/discover/movie", requestDto, 20, "vote_average.desc"));

        return categorizedMovies;
    }

    private List<MovieRecommendationResponseDto> fetchMoviesFromTmdb(String endpoint, MovieRecommendationRequestDto requestDto,
                                                                     int limit, String sortBy) {
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(TMDB_BASE_URL + endpoint)
                .queryParam("api_key", apiKey)
                .queryParam("watch_region", "JP")
                .queryParam("with_watch_providers", requestDto.getProvider())
                .queryParam("with_genres", requestDto.getGenre())
                .queryParam("with_original_language", requestDto.getLanguage())
                .queryParam("language", "ja-JP")
                .queryParam("sort_by", sortBy);

        String url = urlBuilder.toUriString();
        logger.info("📡 TMDb APIリクエスト: {}", url);

        // TMDb APIからデータを取得
        TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);

        // 取得結果から最大 limit 件を取得
        return response != null ? response.toMovieDtoList().stream().limit(limit).collect(Collectors.toList()) : List.of();
    }
}