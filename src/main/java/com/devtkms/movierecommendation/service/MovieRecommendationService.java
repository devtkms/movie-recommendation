package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;
import com.devtkms.movierecommendation.mapper.TagMasterMapper;
import com.devtkms.movierecommendation.response.TmdbResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieRecommendationService {

    private final TagMasterMapper tagMasterMapper;
    private final TmdbApiClient tmdbApiClient;
    private final MovieSelectorService movieSelector;

    public MovieRecommendationService(TagMasterMapper tagMasterMapper, TmdbApiClient tmdbApiClient, MovieSelectorService movieSelector) {
        this.tagMasterMapper = tagMasterMapper;
        this.tmdbApiClient = tmdbApiClient;
        this.movieSelector = movieSelector;
    }

    public MovieRecommendationResultDto recommendMovies(MovieRecommendationRequestDto requestDto) {
        List<String> keywordIds = tagMasterMapper.findKeywordIdsByConditions(
                requestDto.getMood(),
                requestDto.getTone(),
                requestDto.getAfter()
        );

        TmdbResponse response = tmdbApiClient.fetchMoviesByKeywords(keywordIds);
        List<MovieRecommendationResponseDto> allMovies = response.toMovieDtoList();
        List<MovieRecommendationResponseDto> selectedMovies = movieSelector.selectUniqueMovies(allMovies, 20);

        return new MovieRecommendationResultDto(selectedMovies);  // ← ラップして返す
    }
}