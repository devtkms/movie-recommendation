package com.devtkms.movierecommendation.mapper;

import com.devtkms.movierecommendation.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper interface for handling user persistence.
 */
@Mapper
public interface UserMapper {

    /**
     * Inserts a new user into the database.
     *
     * @param user The user entity.
     */
    void insertUser(UserEntity user);

    /**
     * Retrieves a user by their user ID.
     *
     * @param userId The user ID.
     * @return The user entity.
     */
    UserEntity selectUser(String userId);

    /**
     * Retrieves a user by their internal database ID.
     *
     * @param id The internal user ID.
     * @return The user entity.
     */
    UserEntity findById(Long id);
}