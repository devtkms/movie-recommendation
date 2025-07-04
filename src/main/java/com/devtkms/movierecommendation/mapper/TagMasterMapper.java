package com.devtkms.movierecommendation.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Mapper for retrieving TMDb keyword IDs based on user's question answers.
 */
@Mapper
public interface TagMasterMapper {

    /**
     * Finds TMDb keyword IDs based on a combination of mood, tone, and after.
     *
     * @param mood  The user's current mood.
     * @param tone  The tone of the movie.
     * @param after The feeling after watching.
     * @return List of keyword ID strings.
     */
    List<String> findKeywordIdsByConditions(String mood, String tone, String after);
}