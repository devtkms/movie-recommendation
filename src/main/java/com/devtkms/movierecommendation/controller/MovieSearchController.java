package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.response.TmdbResponse;
import com.devtkms.movierecommendation.service.MovieSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that provides search functionality by movie title.
 * Handles movie search via text input by the user.
 */
@RestController
@RequestMapping("/api/search")
@CrossOrigin
public class MovieSearchController {

    /** Movie search service */
    private final MovieSearchService movieSearchService;

    public MovieSearchController(MovieSearchService movieSearchService) {
        this.movieSearchService = movieSearchService;
    }

    /**
     * Endpoint to search for movies by title from TMDb.
     *
     * @param query Movie title or partial title as search keyword
     * @return Search results fetched from TMDb
     */
    @GetMapping("/movies")
    public ResponseEntity<TmdbResponse> searchMovies(@RequestParam("query") String query) {
        TmdbResponse result = movieSearchService.searchByTitle(query);
        return ResponseEntity.ok(result);
    }
}