package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.entity.FavoriteEntity;
import com.devtkms.movierecommendation.mapper.FavoriteMapper;
import com.devtkms.movierecommendation.dto.MovieSaveRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

/**
 * Service responsible for saving and retrieving user's favorite movies.
 */
@Service
public class MovieSaveService {

    private final FavoriteMapper favoriteMapper;
    private static final Logger logger = Logger.getLogger(MovieSaveService.class.getName());

    public MovieSaveService(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    /**
     * Save a movie to the user's favorites list.
     *
     * @param userId User's unique ID
     * @param dto    Movie data to be saved
     */
    public void saveFavorite(String userId, MovieSaveRequestDto dto) {
        FavoriteEntity entity = new FavoriteEntity();
        entity.setUserId(userId);
        entity.setMovieId(dto.getMovieId());
        entity.setTitle(dto.getTitle());
        entity.setPosterPath(dto.getPosterPath());

        favoriteMapper.insertFavorite(entity);
    }

    /**
     * Retrieve all movies saved by the user.
     *
     * @param userId User's unique ID
     * @return List of saved movies
     */
    public List<FavoriteEntity> getFavorites(String userId) {
        logger.info("User ID: " + userId + " accessed the favorites feature");
        return favoriteMapper.selectByUserId(userId);
    }

    /**
     * Delete a movie from the user's favorites list.
     *
     * @param userId  User's unique ID
     * @param movieId ID of the movie to be removed
     */
    public void deleteFavorite(String userId, Long movieId) {
        favoriteMapper.deleteByUserIdAndMovieId(userId, movieId);
    }
}