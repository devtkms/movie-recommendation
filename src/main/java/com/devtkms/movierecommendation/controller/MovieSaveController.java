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
 * Controller for handling movie save, fetch, delete, and detail retrieval.
 * Used to manage favorite movies per authenticated user.
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
     * Saves a movie to the user's favorites.
     *
     * @param user Authenticated user info
     * @param dto DTO of the movie to save
     * @return Success message
     */
    @PostMapping("/save")
    public ResponseEntity<MovieSaveResponseDto> saveMovie(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody MovieSaveRequestDto dto
    ) {
        movieSaveService.saveFavorite(user.getUsername(), dto);
        return ResponseEntity.ok(new MovieSaveResponseDto("Movie saved successfully"));
    }

    /**
     * Retrieves a list of movies saved by the currently logged-in user.
     *
     * @param user Authenticated user info
     * @return List of saved movies
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
     * Deletes a movie from the user's favorites.
     *
     * @param user Authenticated user info
     * @param movieId ID of the movie to delete
     * @return 204 No Content if deletion is successful
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
     * Retrieves movie overview and watch provider information for the given movie ID.
     *
     * @param movieId Target movie ID
     * @return Movie detail information (overview + streaming providers)
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
