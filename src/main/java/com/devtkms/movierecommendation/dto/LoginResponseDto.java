package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO returned upon successful login.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    /** Unique identifier of the logged-in user */
    private Long id;

    /** User's nickname */
    private String nickname;
}