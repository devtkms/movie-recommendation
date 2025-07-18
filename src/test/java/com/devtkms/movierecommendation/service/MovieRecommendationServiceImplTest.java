package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;
import com.devtkms.movierecommendation.mapper.FavoriteMapper;
import com.devtkms.movierecommendation.mapper.TagMasterMapper;
import com.devtkms.movierecommendation.mapper.UserMapper;
import com.devtkms.movierecommendation.response.TmdbResponse;
import com.devtkms.movierecommendation.service.impl.MovieRecommendationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRecommendationServiceImplTest {

    @Mock
    TagMasterMapper tagMasterMapper;

    @Mock
    TmdbApiClient tmdbApiClient;

    @Mock
    MovieSelectorService movieSelector;

    @Mock
    UserMapper userMapper;

    @Mock
    FavoriteMapper favoriteMapper;

    @InjectMocks
    MovieRecommendationServiceImpl movieRecommendationServiceImpl;

    @Test
    void recommendMovies_returnRecommendedList() {
        // Prepare input data
        MovieRecommendationRequestDto requestDto = new MovieRecommendationRequestDto();
        requestDto.setMood("light");
        requestDto.setTone("slow");
        requestDto.setAfter("refresh");
        requestDto.setIsMyData(false);

        String userId = "user123";
        List<String> keywordIds = List.of("100", "200");

        // Mock movie DTOs
        MovieRecommendationResponseDto movie1 = new MovieRecommendationResponseDto();
        movie1.setId(1L);
        movie1.setTitle("Title1");
        movie1.setIsSaved(false);

        MovieRecommendationResponseDto movie2 = new MovieRecommendationResponseDto();
        movie2.setId(2L);
        movie2.setTitle("Title2");
        movie2.setIsSaved(false);

        List<MovieRecommendationResponseDto> allMovies = List.of(movie1, movie2);

        // Define behavior of mock dependencies
        when(tagMasterMapper.findKeywordIdsByConditions("light", "slow", "refresh")).thenReturn(keywordIds);

        TmdbResponse mockResponse = org.mockito.Mockito.mock(TmdbResponse.class);
        when(mockResponse.toMovieDtoList()).thenReturn(allMovies);
        when(tmdbApiClient.fetchMoviesByKeywords(keywordIds)).thenReturn(mockResponse);

        when(movieSelector.selectUniqueMovies(allMovies, 20)).thenReturn(allMovies);
        when(favoriteMapper.selectMovieIdsByUserId(userId)).thenReturn(List.of(1L)); // movie with ID 1L is already saved

        // Execute the method under test
        MovieRecommendationResultDto result = movieRecommendationServiceImpl.recommendMovies(requestDto, userId);

        // Assert the result
        assertEquals(2, result.getCombined().size()); // Expecting 2 movies in the result
        assertTrue(result.getCombined().stream().anyMatch(MovieRecommendationResponseDto::getIsSaved)); // At least one movie is marked as saved
    }
}