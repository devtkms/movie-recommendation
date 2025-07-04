package com.devtkms.movierecommendation.mapper;

import com.devtkms.movierecommendation.entity.FavoriteEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Mapper interface for handling user's saved (favorite) movies.
 */
@Mapper
public interface FavoriteMapper {

    /**
     * Inserts a movie into the user's favorites.
     *
     * @param entity The favorite movie entity.
     */
    void insertFavorite(FavoriteEntity entity);

    /**
     * Retrieves a list of saved movies by user ID.
     *
     * @param userId The ID of the user.
     * @return List of saved movie entities.
     */
    List<FavoriteEntity> selectByUserId(String userId);

    /**
     * Deletes a saved movie by user ID and movie ID.
     *
     * @param userId  The ID of the user.
     * @param movieId The ID of the movie to be removed.
     */
    void deleteByUserIdAndMovieId(String userId, Long movieId);

    /**
     * Retrieves a list of saved movie IDs by user ID.
     *
     * @param userId The ID of the user.
     * @return List of movie IDs.
     */
    List<Long> selectMovieIdsByUserId(String userId);
}