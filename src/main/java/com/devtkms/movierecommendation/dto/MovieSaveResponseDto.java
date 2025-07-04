package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for response after saving a movie.
 */
@Data
@AllArgsConstructor
public class MovieSaveResponseDto {

    /** Message indicating save success */
    private String message;
}