package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * DTO for user login requests.
 */
@Data
public class LoginRequestDto {

    /** Unique user identifier (used as login ID) */
    private String userId;

    /** User's password (used for authentication) */
    private String password;
}