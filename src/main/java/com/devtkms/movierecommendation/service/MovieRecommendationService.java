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

import java.util.List;

@Service
public class MovieRecommendationService {

//    ローカル
//    @Value("${TMDB_API_KEY}")
    // 本番
    @Value("${TMDB_API_KEY}")
    private String apiKey;

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3";

    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(MovieRecommendationService.class);


    @Cacheable(value = "movies", key = "#requestDto.toString() + #page + #size", unless = "#result == null or #result.isEmpty()")
    public List<MovieRecommendationResponseDto> getMovies(MovieRecommendationRequestDto requestDto, int page, int size) {
        logger.info("🔍 APIリクエストを送信中: genre={}, provider={}, page={}", requestDto.getGenre(), requestDto.getProvider(), page);

        // API URLを構築
        String url = UriComponentsBuilder.fromHttpUrl(TMDB_BASE_URL + "/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("with_genres", requestDto.getGenre())
                .queryParam("watch_region", "JP")
                .queryParam("with_watch_providers", requestDto.getProvider())
                .queryParam("with_original_language", requestDto.getLanguage())
                .queryParam("sort_by", "popularity.desc")
                .queryParam("language", "ja-JP")
                .queryParam("page", page) // ページネーション追加
                .queryParam("size", Math.min(size, 30)) // ✅ 30件取得するように変更
                .toUriString();

        // TMDb APIからデータを取得
        TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);

        // DTOに変換して返す（30件取得する）
        return response != null ? response.toMovieDtoList() : List.of();
    }
}