package com.devtkms.movierecommendation.client;

import com.devtkms.movierecommendation.dto.MovieOverviewResponseDto;
import com.devtkms.movierecommendation.response.TmdbResponse;
import com.devtkms.movierecommendation.response.TmdbWatchProviderResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class TmdbApiClient {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private static final Logger logger = Logger.getLogger(TmdbApiClient.class.getName());

    public TmdbApiClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * 指定されたキーワードIDのリストを使って映画を検索し、ランダムに2ページ分の結果を取得する。
     *
     * @param keywordIds TMDbのキーワードIDのリスト
     * @return 検索結果をまとめた {@link TmdbResponse}
     */
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

            logger.info("🎲 TMDb メイン画面から取得 (page=" + page + "): " + url);

            ResponseEntity<TmdbResponse> response = restTemplate.getForEntity(url, TmdbResponse.class);
            if (response.getBody() != null && response.getBody().getResults() != null) {
                allResults.addAll(response.getBody().getResults());
            }
        }

        TmdbResponse mergedResponse = new TmdbResponse();
        mergedResponse.setResults(allResults);
        return mergedResponse;
    }

    /**
     * 指定された映画IDに対応する配信サービス情報を取得する。
     *
     * @param movieId TMDbの映画ID
     * @return 配信サービスの情報を含む {@link TmdbWatchProviderResponse}
     */
    public TmdbWatchProviderResponse fetchWatchProviders(Long movieId) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/movie/" + movieId + "/watch/providers")
                .queryParam("api_key", apiKey)
                .build()
                .toUriString();

        logger.info("📺 TMDb 配信情報を取得 URL: " + url);

        ResponseEntity<TmdbWatchProviderResponse> response =
                restTemplate.getForEntity(url, TmdbWatchProviderResponse.class);
        return response.getBody();
    }

    /**
     * 指定された映画IDをもとに、TMDbのレコメンドAPIから関連映画の情報を取得する。
     *
     * @param movieId TMDbの映画ID
     * @return レコメンドされた映画のリストを含む {@link TmdbResponse}
     */
    public TmdbResponse fetchRecommendationsByMovieId(Long movieId) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/movie/" + movieId + "/recommendations")
                .queryParam("api_key", apiKey)
                .queryParam("language", "ja-JP")
                .build()
                .toUriString();

        logger.info("🎯 TMDb 登録者専用のレコメンド画面から取得 URL: " + url);

        ResponseEntity<TmdbResponse> response = restTemplate.getForEntity(url, TmdbResponse.class);
        return response.getBody();
    }

    /**
     * 映画タイトルでTMDbを検索する。
     *
     * @param title 映画タイトル（部分一致）
     * @return 該当する映画の検索結果 {@link TmdbResponse}
     */
    public TmdbResponse searchMoviesByTitle(String title) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", title)
                .queryParam("language", "ja-JP")
                .build()
                .toUriString();

        logger.info("🔍 TMDb 会員登録画面の映画タイトル検索 URL: " + url);

        ResponseEntity<TmdbResponse> response = restTemplate.getForEntity(url, TmdbResponse.class);
        return response.getBody();
    }

    /**
     * ランダムなトレンド映画（day/week × 1〜3ページ目）を取得する。
     *
     * @return トレンド映画の一覧 {@link TmdbResponse}
     */
    public TmdbResponse fetchRandomTrendingMovies() {
        String timeWindow = ThreadLocalRandom.current().nextBoolean() ? "day" : "week";
        int page = ThreadLocalRandom.current().nextInt(1, 4); // 1〜3

        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/trending/movie/" + timeWindow)
                .queryParam("api_key", apiKey)
                .queryParam("language", "ja-JP")
                .queryParam("page", page)
                .build()
                .toUriString();

        logger.info("🔥 TMDb 登録者専用のレコメンド画面からランダムトレンド取得 URL: " + url);

        ResponseEntity<TmdbResponse> response = restTemplate.getForEntity(url, TmdbResponse.class);
        return response.getBody();
    }

    /**
     * 指定された映画IDの概要、公開日、上映時間、制作国情報を取得する。
     *
     * @param movieId TMDbの映画ID
     * @return 映画の概要情報 {@link MovieOverviewResponseDto}
     */
    @SuppressWarnings("unchecked")
    public MovieOverviewResponseDto fetchMovieOverview(Long movieId) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/movie/" + movieId)
                .queryParam("api_key", apiKey)
                .queryParam("language", "ja-JP")
                .build()
                .toUriString();

        logger.info("📝 TMDb 概要取得 URL: " + url);

        Map<String, Object> result = restTemplate.getForObject(url, Map.class);

        String overview = (String) result.get("overview");
        String releaseDate = (String) result.get("release_date");
        Integer runtime = (Integer) result.get("runtime");

        List<Map<String, String>> countriesRaw = (List<Map<String, String>>) result.get("production_countries");
        List<String> countries = countriesRaw == null ? List.of() :
                countriesRaw.stream()
                        .map(c -> c.get("name"))
                        .collect(Collectors.toList());

        return new MovieOverviewResponseDto(overview, releaseDate, runtime, countries);
    }
}