package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;
import com.devtkms.movierecommendation.entity.QuestionButtonLogEntity;
import com.devtkms.movierecommendation.mapper.QuestionButtonLogMapper;
import com.devtkms.movierecommendation.mapper.TagMasterMapper;
import com.devtkms.movierecommendation.response.TmdbResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieRecommendationService {

    private final TagMasterMapper tagMasterMapper;
    private final TmdbApiClient tmdbApiClient;
    private final MovieSelectorService movieSelector;
    private final QuestionButtonLogMapper questionButtonLogMapper;

    public MovieRecommendationService(TagMasterMapper tagMasterMapper,
                                      TmdbApiClient tmdbApiClient,
                                      MovieSelectorService movieSelector,
                                      QuestionButtonLogMapper questionButtonLogMapper) {
        this.tagMasterMapper = tagMasterMapper;
        this.tmdbApiClient = tmdbApiClient;
        this.movieSelector = movieSelector;
        this.questionButtonLogMapper = questionButtonLogMapper;
    }

    public MovieRecommendationResultDto recommendMovies(MovieRecommendationRequestDto requestDto) {
        QuestionButtonLogEntity log = new QuestionButtonLogEntity();
        log.setMood(requestDto.getMood());
        log.setTone(requestDto.getTone());
        log.setAfter(requestDto.getAfter());
        questionButtonLogMapper.insert(log);

        List<String> keywordIds = tagMasterMapper.findKeywordIdsByConditions(
                requestDto.getMood(),
                requestDto.getTone(),
                requestDto.getAfter()
        );

        TmdbResponse response = tmdbApiClient.fetchMoviesByKeywords(keywordIds);
        List<MovieRecommendationResponseDto> allMovies = response.toMovieDtoList();
        List<MovieRecommendationResponseDto> selectedMovies = movieSelector.selectUniqueMovies(allMovies, 20);

        return new MovieRecommendationResultDto(selectedMovies);
    }
}