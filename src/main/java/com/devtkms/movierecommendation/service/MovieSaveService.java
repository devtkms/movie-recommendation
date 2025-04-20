package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.entity.FavoriteEntity;
import com.devtkms.movierecommendation.mapper.FavoriteMapper;
import com.devtkms.movierecommendation.dto.MovieSaveRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class MovieSaveService {

    private final FavoriteMapper favoriteMapper;
    private static final Logger logger = Logger.getLogger(MovieSaveService.class.getName());

    public MovieSaveService(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    public void saveFavorite(String userId, MovieSaveRequestDto dto) {
        FavoriteEntity entity = new FavoriteEntity();
        entity.setUserId(userId);
        entity.setMovieId(dto.getMovieId());
        entity.setTitle(dto.getTitle());
        entity.setPosterPath(dto.getPosterPath());

        favoriteMapper.insertFavorite(entity);
    }

    public List<FavoriteEntity> getFavorites(String userId) {
        logger.info("ユーザーID: " + userId + "さんが、気になる機能を使用");

        return favoriteMapper.selectByUserId(userId);
    }

    public void deleteFavorite(String userId, Long movieId) {
        favoriteMapper.deleteByUserIdAndMovieId(userId, movieId);
    }
}