package com.devtkms.movierecommendation.service.impl;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;
import com.devtkms.movierecommendation.entity.UserEntity;
import com.devtkms.movierecommendation.mapper.FavoriteMapper;
import com.devtkms.movierecommendation.mapper.QuestionButtonLogMapper;
import com.devtkms.movierecommendation.mapper.TagMasterMapper;
import com.devtkms.movierecommendation.mapper.UserMapper;
import com.devtkms.movierecommendation.response.TmdbResponse;
import com.devtkms.movierecommendation.service.MovieRecommendationService;
import com.devtkms.movierecommendation.service.MovieSelectorService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Service class responsible for business logic related to movie recommendations.
 */
@Service
public class MovieRecommendationServiceImpl implements MovieRecommendationService {

    private final TagMasterMapper tagMasterMapper;
    private final TmdbApiClient tmdbApiClient;
    private final MovieSelectorService movieSelector;
    private final UserMapper userMapper;
    private final FavoriteMapper favoriteMapper;
    private static final Logger logger = Logger.getLogger(MovieRecommendationServiceImpl.class.getName());

    public MovieRecommendationServiceImpl(TagMasterMapper tagMasterMapper,
                                          TmdbApiClient tmdbApiClient,
                                          MovieSelectorService movieSelector,
                                          QuestionButtonLogMapper questionButtonLogMapper,
                                          UserMapper userMapper,
                                          FavoriteMapper favoriteMapper) {
        this.tagMasterMapper = tagMasterMapper;
        this.tmdbApiClient = tmdbApiClient;
        this.movieSelector = movieSelector;
        this.userMapper = userMapper;
        this.favoriteMapper = favoriteMapper;
    }

    /**
     * Main method to recommend movies based on 3-question user input.
     *
     * @param requestDto User's answers (mood, tone, after)
     * @param userId     User identifier (can be null for anonymous)
     * @return A DTO containing the list of recommended movies
     */
    public MovieRecommendationResultDto recommendMovies(MovieRecommendationRequestDto requestDto, String userId) {

        logger.info("User ID: " + userId + ", Mood: " + requestDto.getMood()
                + ", Tone: " + requestDto.getTone() + ", After: " + requestDto.getAfter());

        // Retrieve keyword IDs based on question responses
        List<String> keywordIds = tagMasterMapper.findKeywordIdsByConditions(
                requestDto.getMood(),
                requestDto.getTone(),
                requestDto.getAfter()
        );

        // Fetch movie data from TMDb API
        TmdbResponse response = tmdbApiClient.fetchMoviesByKeywords(keywordIds);
        List<MovieRecommendationResponseDto> allMovies = response.toMovieDtoList();

        // Select 20 unique movies from the results
        List<MovieRecommendationResponseDto> selectedMovies = movieSelector.selectUniqueMovies(allMovies, 20);

        // Mark saved movies for logged-in user
        if (userId != null) {
            Set<Long> savedIds = new HashSet<>(favoriteMapper.selectMovieIdsByUserId(userId));
            for (MovieRecommendationResponseDto movie : selectedMovies) {
                if (savedIds.contains(movie.getId())) {
                    movie.setIsSaved(true);
                }
            }
        }

        return new MovieRecommendationResultDto(selectedMovies);
    }

    /**
     * Recommend movies based on the user's favorite movie.
     * If favorite is not set, return trending movies only.
     *
     * @param userId User's DB ID
     * @param usrId  User's login ID
     * @return A DTO containing personalized movie recommendations
     */
    public MovieRecommendationResultDto getPersonalizeMovies(Long userId, String usrId) {

        logger.info("User ID: " + userId + " accessed personalized recommendation.");

        UserEntity user = userMapper.findById(userId);
        Long favoriteMovieId = user.getFavoriteMovieId();

        // If favorite movie is not set, return only trending movies
        if (favoriteMovieId == null) {
            TmdbResponse trendResponse = tmdbApiClient.fetchRandomTrendingMovies();
            List<MovieRecommendationResponseDto> trending = trendResponse.toMovieDtoList();
            return new MovieRecommendationResultDto(trending);
        }

        // Fetch recommendations based on favorite movie
        TmdbResponse recommendResponse = tmdbApiClient.fetchRecommendationsByMovieId(favoriteMovieId);

        // Fetch trending movies
        TmdbResponse trendResponse = tmdbApiClient.fetchRandomTrendingMovies();

        // Convert to DTOs
        List<MovieRecommendationResponseDto> recommended = recommendResponse.toMovieDtoList();
        List<MovieRecommendationResponseDto> trending = trendResponse.toMovieDtoList();

        // Merge and shuffle
        recommended.addAll(trending);
        Collections.shuffle(recommended);

        // Mark saved movies for logged-in user
        if (userId != null) {
            Set<Long> savedIds = new HashSet<>(favoriteMapper.selectMovieIdsByUserId(usrId));
            for (MovieRecommendationResponseDto recommend : recommended) {
                if (savedIds.contains(recommend.getId())) {
                    recommend.setIsSaved(true);
                }
            }
        }

        return new MovieRecommendationResultDto(recommended);
    }
}