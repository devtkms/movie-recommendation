package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility service that filters out duplicates from a movie list and selects a random subset.
 */
@Component
public class MovieSelectorService {

    /**
     * Removes duplicates and selects up to `limit` number of movies randomly.
     *
     * @param movies List of candidate movies (may include duplicates)
     * @param limit  Maximum number of movies to return
     * @return Randomly shuffled list with no duplicates (up to `limit` items)
     */
    public List<MovieRecommendationResponseDto> selectUniqueMovies(List<MovieRecommendationResponseDto> movies, int limit) {
        List<MovieRecommendationResponseDto> unique = new ArrayList<>(movies.stream()
                .distinct()
                .toList());

        Collections.shuffle(unique);

        return unique.stream()
                .limit(limit)
                .toList();
    }
}