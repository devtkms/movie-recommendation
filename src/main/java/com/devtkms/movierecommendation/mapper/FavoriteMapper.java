package com.devtkms.movierecommendation.mapper;

import com.devtkms.movierecommendation.entity.FavoriteEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    void insertFavorite(FavoriteEntity entity);
    List<FavoriteEntity> selectByUserId(String userId);
    void deleteByUserIdAndMovieId(String userId, Long movieId);
    List<Long> selectMovieIdsByUserId(String userId);
}