package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.WatchProviderResponseDto;
import com.devtkms.movierecommendation.service.MovieWatchProviderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 指定された映画に対応する配信サービス（Watch Providers）を返すコントローラー
 */
@RestController
@RequestMapping("/movie")
public class MovieWatchProviderController {

    private final MovieWatchProviderService providerService;

    public MovieWatchProviderController(MovieWatchProviderService providerService) {
        this.providerService = providerService;
    }

    /**
     * 指定された映画IDに対して、日本で視聴可能な配信サービス一覧を取得する
     *
     * @param movieId TMDbの映画ID
     * @return 対応する配信サービスのDTOリスト（例: Netflix, Prime Videoなど）
     */
    @GetMapping("/{movieId}/watch/providers")
    public List<WatchProviderResponseDto> getWatchProviders(@PathVariable Long movieId) {
        return providerService.getWatchProviders(movieId);
    }
}