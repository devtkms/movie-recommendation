package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.*;
import com.devtkms.movierecommendation.entity.FavoriteEntity;
import com.devtkms.movierecommendation.security.CustomUserDetails;
import com.devtkms.movierecommendation.service.MovieOverviewService;
import com.devtkms.movierecommendation.service.MovieSaveService;
import com.devtkms.movierecommendation.service.MovieWatchProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 映画の保存・取得・削除・詳細表示を扱うコントローラー。
 * ログインユーザーごとのお気に入り映画の管理に使用される。
 */
@RestController
@RequestMapping("/api/movies")
public class MovieSaveController {

    private final MovieSaveService movieSaveService;
    private final MovieOverviewService movieOverviewService;
    private final MovieWatchProviderService movieWatchProviderService;

    public MovieSaveController(MovieSaveService movieSaveService, MovieOverviewService movieOverviewService, MovieWatchProviderService movieWatchProviderService) {
        this.movieSaveService = movieSaveService;
        this.movieOverviewService = movieOverviewService;
        this.movieWatchProviderService = movieWatchProviderService;
    }

    /**
     * 映画をお気に入りとして保存する。
     *
     * @param user ログインユーザー情報（認証済み）
     * @param dto 保存対象映画の情報
     * @return 保存完了メッセージ
     */
    @PostMapping("/save")
    public ResponseEntity<MovieSaveResponseDto> saveMovie(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody MovieSaveRequestDto dto
    ) {
        movieSaveService.saveFavorite(user.getUsername(), dto);
        return ResponseEntity.ok(new MovieSaveResponseDto("保存しました"));
    }

    /**
     * 現在ログインしているユーザーのお気に入り映画一覧を取得する。
     *
     * @param user ログインユーザー情報
     * @return 保存済み映画のリスト
     */
    @GetMapping("/saved")
    public ResponseEntity<List<MovieSavedResponseDto>> getSavedMovies(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<FavoriteEntity> favorites = movieSaveService.getFavorites(user.getUsername());

        List<MovieSavedResponseDto> result = favorites.stream()
                .map(fav -> new MovieSavedResponseDto(fav.getMovieId(), fav.getTitle(), fav.getPosterPath()))
                .toList();

        return ResponseEntity.ok(result);
    }

    /**
     * お気に入りから映画を削除する。
     *
     * @param user ログインユーザー情報
     * @param movieId 削除対象の映画ID
     * @return 204 No Content（削除完了）
     */
    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<Void> deleteMovie(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long movieId
    ) {
        movieSaveService.deleteFavorite(user.getUsername(), movieId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 映画IDに基づいて概要および配信サービス情報をまとめて取得する。
     *
     * @param movieId 対象映画のID
     * @return 映画の詳細情報（概要 + 配信サービス）
     */
    @GetMapping("/{movieId}/detail")
    public ResponseEntity<MovieDetailResponseDto> getMovieDetail(@PathVariable Long movieId) {
        MovieOverviewResponseDto overview = movieOverviewService.getMovieOverview(movieId);
        List<WatchProviderResponseDto> providers = movieWatchProviderService.getWatchProviders(movieId);

        MovieDetailResponseDto dto = new MovieDetailResponseDto();
        dto.setOverview(overview);
        dto.setProviders(providers);

        return ResponseEntity.ok(dto);
    }
}
