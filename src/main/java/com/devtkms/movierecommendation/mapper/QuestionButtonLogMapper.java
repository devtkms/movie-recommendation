package com.devtkms.movierecommendation.mapper;

import com.devtkms.movierecommendation.entity.QuestionButtonLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper for logging user responses to movie recommendation questions.
 */
@Mapper
public interface QuestionButtonLogMapper {

    /**
     * Inserts a log of the user's answers to the recommendation questions.
     *
     * @param entity The entity containing mood, tone, and after values.
     */
    void insert(QuestionButtonLogEntity entity);
}