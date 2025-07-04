package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.UserRegisterRequestDto;
import com.devtkms.movierecommendation.entity.UserEntity;
import com.devtkms.movierecommendation.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service class that handles user registration and retrieval.
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user with encrypted password and user profile.
     *
     * @param userDto The DTO containing user registration information
     * @return The newly created user entity
     */
    public UserEntity registerUser(UserRegisterRequestDto userDto) {
        // Check for duplicate user ID
        if (userMapper.selectUser(userDto.getUserId()) != null) {
            throw new RuntimeException("This user ID is already registered");
        }

        // Encode the password
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());

        // Map DTO to Entity
        UserEntity user = new UserEntity();
        user.setUserId(userDto.getUserId());
        user.setPassword(hashedPassword);
        user.setNickname(userDto.getNickname());
        user.setUseProviderName(userDto.getUseProviderName());
        user.setUseProviderId(userDto.getUseProviderId());
        user.setFavoriteMovieName(userDto.getFavoriteMovieName());
        user.setFavoriteMovieId(userDto.getFavoriteMovieId());
        user.setGender(userDto.getGender());
        user.setAgeGroup(userDto.getAgeGroup());
        user.setProvider("local");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Save to DB
        userMapper.insertUser(user);
        return user;
    }

    /**
     * Retrieve a user by their user ID.
     *
     * @param userId The user ID
     * @return The user entity if found
     */
    public UserEntity findByUserId(String userId) {
        UserEntity user = userMapper.selectUser(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }
}