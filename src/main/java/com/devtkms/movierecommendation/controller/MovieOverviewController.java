package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.MovieOverviewRequestDto;
import com.devtkms.movierecommendation.dto.MovieOverviewResponseDto;
import com.devtkms.movierecommendation.service.MovieOverviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 映画の概要情報を取得するAPIコントローラー。
 */
@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieOverviewController {

    /** 映画概要取得サービス */
    private final MovieOverviewService movieOverviewService;

    /**
     * 指定された映画IDに基づいて映画の概要情報を取得するエンドポイント。
     *
     * @param request 映画IDを含むリクエストDTO
     * @return 映画の概要情報を含むレスポンスDTO
     */
    @PostMapping("/overview")
    public MovieOverviewResponseDto getMovieOverview(@RequestBody MovieOverviewRequestDto request) {
        return movieOverviewService.getMovieOverview(request.getMovieId());
    }
}
