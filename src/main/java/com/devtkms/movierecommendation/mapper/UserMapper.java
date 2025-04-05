package com.devtkms.movierecommendation.mapper;

import com.devtkms.movierecommendation.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void insertUser(UserEntity user);
    UserEntity selectUser(String email);
}