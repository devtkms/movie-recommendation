package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.entity.FavoriteEntity;
import com.devtkms.movierecommendation.mapper.FavoriteMapper;
import com.devtkms.movierecommendation.dto.MovieSaveRequestDto;
import org.springframework.stereotype.Service;

@Service
public class MovieSaveService {

    private final FavoriteMapper favoriteMapper;

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
}