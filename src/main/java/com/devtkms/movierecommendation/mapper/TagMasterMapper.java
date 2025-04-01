package com.devtkms.movierecommendation.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMasterMapper {
    List<String> findKeywordIdsByConditions(
            String mood,
            String tone,
            String after
    );
}