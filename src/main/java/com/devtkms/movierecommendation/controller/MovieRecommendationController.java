package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;
import com.devtkms.movierecommendation.entity.UserEntity;
import com.devtkms.movierecommendation.service.MovieRecommendationService;
import com.devtkms.movierecommendation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 映画推薦に関するリクエストを受け付けるコントローラー
 */
@RestController
@RequestMapping("/api/recommendations")
public class MovieRecommendationController {

    private final MovieRecommendationService recommendationService;
    private final UserService userService;

    public MovieRecommendationController(MovieRecommendationService recommendationService, UserService userService) {
        this.recommendationService = recommendationService;
        this.userService = userService;
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
    public ResponseEntity<MovieRecommendationResultDto> getPersonalizeMovies(Authentication authentication) {
        // トークンに含まれる userId からユーザー情報を取得
        String userId = authentication.getName();
        UserEntity user = userService.findByUserId(userId);

        MovieRecommendationResultDto response = recommendationService.getPersonalizeMovies(user.getId());

        return ResponseEntity.ok(response);
    }
}