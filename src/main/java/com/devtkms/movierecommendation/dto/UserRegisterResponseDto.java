package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for response after user registration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterResponseDto {

    /** Registered user's internal ID */
    private Long id;

    /** Registered user's nickname */
    private String nickname;
}