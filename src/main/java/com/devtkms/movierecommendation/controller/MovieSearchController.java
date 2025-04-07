package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.response.TmdbResponse;
import com.devtkms.movierecommendation.service.MovieSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@CrossOrigin
public class MovieSearchController {

    private final MovieSearchService movieSearchService;

    public MovieSearchController(MovieSearchService movieSearchService) {
        this.movieSearchService = movieSearchService;
    }

    @GetMapping("/movies")
    public ResponseEntity<TmdbResponse> searchMovies(@RequestParam("query") String query) {
        TmdbResponse result = movieSearchService.searchByTitle(query);
        return ResponseEntity.ok(result);
    }
}