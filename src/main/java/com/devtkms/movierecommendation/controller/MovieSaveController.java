package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.MovieSaveRequestDto;
import com.devtkms.movierecommendation.dto.MovieSaveResponseDto;
import com.devtkms.movierecommendation.security.CustomUserDetails;
import com.devtkms.movierecommendation.service.MovieSaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieSaveController {

    private final MovieSaveService movieSaveService;

    public MovieSaveController(MovieSaveService movieSaveService) {
        this.movieSaveService = movieSaveService;
    }

    @PostMapping("/save")
    public ResponseEntity<MovieSaveResponseDto> saveMovie(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody MovieSaveRequestDto dto
    ) {
        movieSaveService.saveFavorite(user.getUsername(), dto);
        return ResponseEntity.ok(new MovieSaveResponseDto("保存しました"));
    }
}
