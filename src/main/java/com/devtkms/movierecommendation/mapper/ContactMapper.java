package com.devtkms.movierecommendation.mapper;

import com.devtkms.movierecommendation.entity.ContactEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContactMapper {
    void insert(ContactEntity entity);
}
