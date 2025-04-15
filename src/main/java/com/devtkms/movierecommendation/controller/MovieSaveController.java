package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.*;
import com.devtkms.movierecommendation.entity.FavoriteEntity;
import com.devtkms.movierecommendation.response.TmdbWatchProviderResponse;
import com.devtkms.movierecommendation.security.CustomUserDetails;
import com.devtkms.movierecommendation.service.MovieOverviewService;
import com.devtkms.movierecommendation.service.MovieSaveService;
import com.devtkms.movierecommendation.service.MovieWatchProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/save")
    public ResponseEntity<MovieSaveResponseDto> saveMovie(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody MovieSaveRequestDto dto
    ) {
        movieSaveService.saveFavorite(user.getUsername(), dto);
        return ResponseEntity.ok(new MovieSaveResponseDto("保存しました"));
    }

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

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<Void> deleteMovie(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long movieId
    ) {
        movieSaveService.deleteFavorite(user.getUsername(), movieId);
        return ResponseEntity.noContent().build();
    }

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
