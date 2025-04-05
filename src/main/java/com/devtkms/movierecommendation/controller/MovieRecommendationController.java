package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;
import com.devtkms.movierecommendation.service.MovieRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 映画推薦に関するリクエストを受け付けるコントローラー
 */
@RestController
@RequestMapping("/api/recommendations")
public class MovieRecommendationController {

    private final MovieRecommendationService recommendationService;

    public MovieRecommendationController(MovieRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * 映画推薦の取得エンドポイント
     *
     * フロントエンドから送信された質問3つの回答をもとに、
     * 映画を推薦して返却する。
     *
     * @param requestDto ユーザーの質問回答（mood, tone, after）
     * @return 推薦映画のリストを含む結果DTO
     */
    @PostMapping
    public ResponseEntity<MovieRecommendationResultDto> getRecommendations(
            @RequestBody MovieRecommendationRequestDto requestDto) {

        // 質問回答を元に映画推薦ロジックを実行
        MovieRecommendationResultDto response = recommendationService.recommendMovies(requestDto);

        // 結果をレスポンスとして返却
        return ResponseEntity.ok(response);
    }

    /**
     * トレンド映画を取得するエンドポイント
     *
     * ユーザーの質問回答なしで、システム側でトレンド映画を推薦する
     *
     * @return トレンド映画のリストを含む結果DTO
     */
    @GetMapping("/personalize")
    public ResponseEntity<MovieRecommendationResultDto> getPersonalizeMovies() {
        // トレンド映画を取得するロジックを実行
        MovieRecommendationResultDto response = recommendationService.getPersonalizeMovies();

        // 結果をレスポンスとして返却
        return ResponseEntity.ok(response);
    }
}