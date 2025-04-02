package com.devtkms.movierecommendation.mapper;

import com.devtkms.movierecommendation.entity.QuestionButtonLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionButtonLogMapper {
    void insert(QuestionButtonLogEntity entity);
}