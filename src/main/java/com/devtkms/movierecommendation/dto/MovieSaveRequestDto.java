package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * 映画推薦リクエストで送信される質問の回答内容を保持するDTO
 */
@Data
public class MovieSaveRequestDto {
    private Long movieId;
    private String title;
    private String posterPath;
}