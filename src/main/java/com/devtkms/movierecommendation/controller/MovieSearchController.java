package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.response.TmdbResponse;
import com.devtkms.movierecommendation.service.MovieSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 映画タイトルによる検索を提供するコントローラー。
 * ユーザーが文字列で映画を検索する機能を担う。
 */
@RestController
@RequestMapping("/api/search")
@CrossOrigin
public class MovieSearchController {

    /** 映画検索サービス */
    private final MovieSearchService movieSearchService;

    public MovieSearchController(MovieSearchService movieSearchService) {
        this.movieSearchService = movieSearchService;
    }

    /**
     * タイトルに基づいてTMDbから映画を検索するエンドポイント。
     *
     * @param query 映画タイトルまたはその一部の検索キーワード
     * @return TMDbから取得した検索結果
     */
    @GetMapping("/movies")
    public ResponseEntity<TmdbResponse> searchMovies(@RequestParam("query") String query) {
        TmdbResponse result = movieSearchService.searchByTitle(query);
        return ResponseEntity.ok(result);
    }
}