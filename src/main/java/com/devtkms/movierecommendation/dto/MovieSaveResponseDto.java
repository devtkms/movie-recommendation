package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 映画保存時のレスポンスDTO
 */
@Data
@AllArgsConstructor
public class MovieSaveResponseDto {
    private String message;
}