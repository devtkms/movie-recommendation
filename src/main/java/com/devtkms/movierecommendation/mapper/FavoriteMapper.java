package com.devtkms.movierecommendation.mapper;

import com.devtkms.movierecommendation.entity.FavoriteEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper {
    void insertFavorite(FavoriteEntity entity);
}